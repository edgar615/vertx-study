package com.edgar.vertx.service.discovery.start;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * Created by edgar on 16-6-29.
 */
public class ServiceDiscoveryVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", ServiceDiscoveryVerticle.class.getName(), "--cluster");
//        ClusterManager mgr = new HazelcastClusterManager();
//        VertxOptions options = new VertxOptions().setClusterManager(mgr);
//        Vertx.clusteredVertx(options, res -> {
//            if (res.succeeded()) {
//                Vertx vertx = res.result();
//                vertx.deployVerticle(ServiceDiscoveryVerticle.class.getName());
//            } else {
//                // failed!
//            }
//        });
  }

  @Override
  public void start() throws Exception {
    //Creating a service discovery instance
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

// Customize the configuration
//        discovery = ServiceDiscovery.create(vertx,
//                new ServiceDiscoveryOptions()
//                        .setAnnounceAddress("service-announce")
//                        .setName("my-name"));

    //Publishing services
    Record record = new Record()
            .setType("eventbus-service-proxy")
            .setLocation(new JsonObject().put("endpoint", "the-service-address"))
            .setName("my-service")
            .setMetadata(new JsonObject().put("some-label", "some-value"));
    System.out.println(record.getRegistration());
    discovery.publish(record, ar -> {
      if (ar.succeeded()) {
        Record publishedRecord = ar.result();
        System.out.println(record.getRegistration());
        System.out.println(publishedRecord.getLocation());
        System.out.println(publishedRecord.getType());
      } else {

      }
    });

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

//        System.out.println(record.getRegistration());
//        discovery.unpublish(record.getRegistration(), ar -> {
//            if (ar.succeeded()) {
//                // Ok
//                System.out.println("unpublish ok");
//            } else {
//                System.out.println("unpublish failed");
//                // cannot un-publish the service, may have already been removed, or the record
// is not published
//            }
//        });
//        System.out.println(record.getRegistration());
//        discovery.unpublish(record.getRegistration(), ar -> {
//            if (ar.succeeded()) {
//                // Ok
//                System.out.println("unpublish ok");
//            } else {
//                System.out.println("unpublish failed");
//                // cannot un-publish the service, may have already been removed, or the record
// is not published
//            }
//        });

    //Looking for service
    discovery.getRecords(r -> true, ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
      } else {

      }
    });
    discovery.close();
  }
}
