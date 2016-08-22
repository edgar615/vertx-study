package com.edgar.vertx.service.discovery.consul;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.consul.ConsulServiceImporter;
import io.vertx.servicediscovery.consul.ImportedConsulService;
import io.vertx.servicediscovery.impl.ServiceTypes;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * 因为vert.x自带的ConsulServiceImporter在读取到节点之后值取了第一条数据.在官方版本未修复之前，采用自己实现的方案来实现（绝大部分代码都是复制的官方代码）.
 * <p/>
 * <code>
 * private String importService(JsonArray array, Future<Void> future) {
 * if (array.isEmpty()) {
 * Future.failedFuture("no service with the given name");
 * return null;
 * } else {
 * JsonObject jsonObject = array.getJsonObject(0);
 * String address = jsonObject.getString("Address");
 * String name = jsonObject.getString("ServiceName");
 * String id = jsonObject.getString("ServiceID");
 * ......
 * }
 * }
 * <p/>
 * </code>
 *
 * @author Edgar  Date 2016/8/5
 */
public class MyConsulServiceImporter implements ServiceImporter {

    private ServicePublisher publisher;
    private HttpClient client;

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsulServiceImporter.class);

    private final List<ImportedConsulService> imports = new ArrayList<>();
    private String dc;
    private long scanTask = -1;

    @Override
    public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Future<Void> completion) {
        this.publisher = publisher;

        HttpClientOptions options = new HttpClientOptions(configuration);
        String host = configuration.getString("host", "localhost");
        int port = configuration.getInteger("port", 8500);

        options.setDefaultHost(host);
        options.setDefaultPort(port);

        dc = configuration.getString("dc");
        client = vertx.createHttpClient(options);

        Future<Void> imports = Future.future();

        retrieveServicesFromConsul(imports);

        imports.setHandler(ar -> {
            if (ar.succeeded()) {
                Integer period = configuration.getInteger("scan-period", 2000);
                if (period != 0) {
                    scanTask = vertx.setPeriodic(period, l -> {
                        Future<Void> future = Future.future();
                        future.setHandler(ar2 -> {
                            if (ar2.failed()) {
                                LOGGER.warn("Consul importation has failed", ar.cause());
                            }
                        });
                        retrieveServicesFromConsul(future);
                    });
                }

                completion.complete();
            } else {
                completion.fail(ar.cause());
            }
        });

    }


    private Handler<Throwable> getErrorHandler(Future future) {
        return t -> {
            if (future != null) {
                future.fail(t);
            } else {
                LOGGER.error(t);
            }
        };
    }

    private void retrieveServicesFromConsul(Future<Void> completed) {
        String path = "/v1/catalog/services";
        if (dc != null) {
            path += "?dc=" + dc;
        }

        Handler<Throwable> error = getErrorHandler(completed);

        client.get(path)
                .exceptionHandler(error)
                .handler(response -> {
                    response
                            .exceptionHandler(error)
                            .bodyHandler(buffer -> {
                                retrieveIndividualServices(buffer.toJsonObject(), completed);
                            });
                })
                .end();
    }

    private void retrieveIndividualServices(JsonObject jsonObject, Future<Void> completed) {
        System.out.println(jsonObject);
        List<String> ids = new ArrayList<>();

        List<Future> futures = new ArrayList<>();
        jsonObject.fieldNames().stream().forEach(name -> {
            Future<Void> future = Future.future();
            Handler<Throwable> error = getErrorHandler(future);
            String path = "/v1/catalog/service/" + name;
            if (dc != null) {
                path += "?dc=" + dc;
            }


            client.get(path)
                    .exceptionHandler(error)
                    .handler(response -> {
                        response.exceptionHandler(error)
                                .bodyHandler(buffer -> {
                                    System.out.println(buffer.toJsonArray());
                                    Future<List<String>> idsFutrue = Future.future();
                                    importService(buffer.toJsonArray(), idsFutrue);
                                    idsFutrue.setHandler(ar -> {
                                        if (ar.succeeded()) {
                                            ids.addAll(ar.result());
                                            System.out.println("id:" + ar.result());
                                            future.complete();
                                        } else {
                                            future.fail(ar.cause());
                                        }
                                    });

//                                    if (id != null) {
//                                        ids.addAll(id);
//                                    }
                                });
                    })
                    .end();

            futures.add(future);
        });

        CompositeFuture.all(futures).setHandler(ar -> {
            if (ar.failed()) {
                LOGGER.error("Fail to retrieve the services from consul", ar.cause());
            } else {
                List<ImportedConsulService> toRemove = new ArrayList<>();
                System.out.println(ids);
                imports.stream().filter(svc -> !ids.contains(svc.id())).forEach(svc -> {
                    System.out.println(svc.id());
                    toRemove.add(svc);
                    svc.unregister(publisher, null);
                });
                imports.removeAll(toRemove);
            }

            if (ar.succeeded()) {
                completed.complete();
            } else {
                completed.fail(ar.cause());
            }
        });
    }

    private List<String> importService(JsonArray array, Future<List<String>> completed) {
        List<String> ids = new ArrayList<>(array.size());
        if (array.isEmpty()) {
            Future.failedFuture("no service with the given name");
        } else {
            List<Future> futures = new ArrayList<>(array.size());
            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject = array.getJsonObject(i);
                String address = jsonObject.getString("Address");
                String name = jsonObject.getString("ServiceName");
                String id = jsonObject.getString("ServiceID");

                JsonArray tags = jsonObject.getJsonArray("ServiceTags");
                if (tags == null) {
                    tags = new JsonArray();
                }
                String path = jsonObject.getString("ServiceAddress");
                int port = jsonObject.getInteger("ServicePort");

                JsonObject metadata = jsonObject.copy();
                tags.stream().forEach(tag -> metadata.put((String) tag, true));

                Record record = new Record()
                        .setName(name)
                        .setMetadata(metadata);

                // To determine the record type, check if we have a tag with a "type" name
                record.setType(ServiceType.UNKNOWN);
                ServiceTypes.all().forEachRemaining(type -> {
                    if (metadata.getBoolean(type.name(), false)) {
                        record.setType(type.name());
                    }
                });

                JsonObject location = new JsonObject();
                location.put("host", address);
                location.put("port", port);
                if (path != null) {
                    location.put("path", path);
                }

                // Manage HTTP endpoint
                if (record.getType().equals("http-endpoint")) {
                    if (path != null) {
                        location.put("root", path);
                    }
                    if (metadata.getBoolean("ssl", false)) {
                        location.put("ssl", true);
                    }
                    location = new HttpLocation(location).toJson();
                }

                record.setLocation(location);

                // the id must be unique, so check if the service has already being imported
                ImportedConsulService imported = getImportedServiceById(id);
                Future<Void> serviceFuture = Future.future();
                if (imported != null) {
                    serviceFuture.complete();
                } else {
                    LOGGER.info("Importing service " + record.getName() + " from consul");
                    imports.add(new ImportedConsulService(name, id, record).register(publisher, serviceFuture));
                }
                ids.add(id);
                futures.add(serviceFuture);
            }
            CompositeFuture.all(futures).setHandler(ar -> {
                if (ar.failed()) {
                    LOGGER.error("Fail to retrieve the services from consul", ar.cause());
                }
                if (ar.succeeded()) {
                    completed.complete(ids);
                } else {
                    completed.fail(ar.cause());
                }
            });

        }
        return ids;

    }

    private ImportedConsulService getImportedServiceById(String id) {
        for (ImportedConsulService svc : imports) {
            if (svc.id().equals(id)) {
                return svc;
            }
        }
        return null;
    }

    @Override
    public void stop(Vertx vertx, ServicePublisher publisher, Future<Void> future) {
        if (scanTask != -1) {
            vertx.cancelTimer(scanTask);
        }
        // Remove all the services that has been imported
        List<Future> list = new ArrayList<>();
        imports.stream().forEach(imported -> {
            Future<Void> fut = Future.future();
            fut.setHandler(ar -> {
                LOGGER.info("Unregistering " + imported.name());
                if (ar.succeeded()) {
                    list.add(Future.succeededFuture());
                } else {
                    list.add(Future.failedFuture(ar.cause()));
                }
            });
            imported.unregister(publisher, fut);
        });

        CompositeFuture.all(list).setHandler(ar -> {
            if (ar.succeeded()) {
                future.complete();
            } else {
                future.fail(ar.cause());
            }
        });
    }
}
