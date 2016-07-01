package com.edgar.vertx.service.discovery.consul;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.consul.ConsulServiceImporter;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar on 16-6-29.
 */
public class HttpServiceVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", HttpServiceVerticle.class.getName());
    }
    ServiceDiscovery discovery;
    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx)
                .registerServiceImporter(new ConsulServiceImporter(), new JsonObject()
                        .put("host", "10.4.7.15")
                        .put("port", 8500)
                        .put("scan-period", 2000));

        //服务关闭之后，redis中的值仍然存在
        Record httpRecord = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
        System.out.println(httpRecord.getRegistration());
        discovery.publish(httpRecord, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(httpRecord.getRegistration());
                System.out.println(publishedRecord.getLocation());
                System.out.println(publishedRecord.getType());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });
        discovery.close();
    }

//    @Override
//    public void stop() throws Exception {
//        if (discovery != null) {
//            String id = map.get("id");
//            System.out.println(id);
//            discovery.unpublish(id, ar -> {
//                if (ar.succeeded()) {
//                    System.out.println("unpublish");
//                    discovery.close();
//                } else {
//                    System.out.println("unpublish failed");
//                    ar.cause().printStackTrace();
//                    discovery.close();
//                }
//            });
//        }
//    }
}
