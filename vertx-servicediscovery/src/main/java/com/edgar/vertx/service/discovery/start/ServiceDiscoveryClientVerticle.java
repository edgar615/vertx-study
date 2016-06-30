package com.edgar.vertx.service.discovery.start;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.servicediscovery.ServiceDiscovery;

/**
 * Created by edgar on 16-6-29.
 */
public class ServiceDiscoveryClientVerticle extends AbstractVerticle {

    public static void main(String[] args) {
      new Launcher().execute("run", ServiceDiscoveryClientVerticle.class.getName(), "--cluster");
//        ClusterManager mgr = new HazelcastClusterManager();
//        VertxOptions options = new VertxOptions().setClusterManager(mgr);
//        Vertx.clusteredVertx(options, res -> {
//            if (res.succeeded()) {
//                Vertx vertx = res.result();
//                vertx.deployVerticle(ServiceDiscoveryClientVerticle.class.getName());
//            } else {
//                // failed!
//            }
//        });
    }

    @Override
    public void start() throws Exception {
        //Creating a service discovery instance
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        //Looking for service
        discovery.getRecords(r -> true, ar -> {
            if (ar.succeeded()) {
                System.out.println(ar.result());
            } else {

            }
        });


//        You can retrieve a single record or all matching record with getRecords. By default, record lookup does includes only records with a status set to UP. This can be overridden:
//
//        when using JSON filter, just set status to the value you want (or * to accept all status)
//
//        when using function, set the includeOutOfService parameter to true in getRecords .
//


//        { "name" = "a" } => matches records with name set fo "a"
//        { "color" = "*" } => matches records with "color" set
//        { "color" = "red" } => only matches records with "color" set to "red"
//        { "color" = "red", "name" = "a"} => only matches records with name set to "a", and color set to "red"



//        discovery.getRecord((JsonObject) null, ar -> {
//            if (ar.succeeded()) {
//                if (ar.result() != null) {
//                    // we have a record
//                } else {
//                    // the lookup succeeded, but no matching service
//                }
//            } else {
//                // lookup failed
//            }
//        });
//
//
//// Get a record by name
//        discovery.getRecord(r -> r.getName().equals("some-name"), ar -> {
//            if (ar.succeeded()) {
//                if (ar.result() != null) {
//                    // we have a record
//                } else {
//                    // the lookup succeeded, but no matching service
//                }
//            } else {
//                // lookup failed
//            }
//        });
//
//        discovery.getRecord(new JsonObject().put("name", "some-service"), ar -> {
//            if (ar.succeeded()) {
//                if (ar.result() != null) {
//                    // we have a record
//                } else {
//                    // the lookup succeeded, but no matching service
//                }
//            } else {
//                // lookup failed
//            }
//        });
//
//// Get all records matching the filter
//        discovery.getRecords(r -> "some-value".equals(r.getMetadata().getString("some-label")), ar -> {
//            if (ar.succeeded()) {
//                List<Record> results = ar.result();
//                // If the list is not empty, we have matching record
//                // Else, the lookup succeeded, but no matching service
//            } else {
//                // lookup failed
//            }
//        });
//
//
//        discovery.getRecords(new JsonObject().put("some-label", "some-value"), ar -> {
//            if (ar.succeeded()) {
//                List<Record> results = ar.result();
//                // If the list is not empty, we have matching record
//                // Else, the lookup succeeded, but no matching service
//            } else {
//                // lookup failed
//            }
//        });
        discovery.close();
    }
}
