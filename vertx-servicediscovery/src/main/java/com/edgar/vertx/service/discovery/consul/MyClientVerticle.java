package com.edgar.vertx.service.discovery.consul;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

import java.util.List;

/**
 * Created by edgar on 16-6-29.
 */
public class MyClientVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", MyClientVerticle.class.getName());
    }
    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx)
                .registerServiceImporter(new MyConsulServiceImporter(), new JsonObject()
                        .put("host", "10.11.0.31")
                        .put("port", 8500)
                        .put("scan-period", 2000));

//       vertx.setTimer(1000, l -> {
//           discovery.getRecords(r -> true, ar -> {
//               if (ar.succeeded()) {
//                   List<Record> records = ar.result();
//                   for (Record record : records) {
//                       System.out.println(record.getType());
//                       System.out.println(record.getLocation());
//                       System.out.println(record.getMetadata());
//                   }
//               }
//           });
//       });

//        vertx.setPeriodic(2000, l -> {
//            discovery.getRecords(new JsonObject().put("ServiceName", "db"), ar -> {
//                if (ar.succeeded()) {
//                    List<Record> records = ar.result();
//                    for (Record record : records) {
//                        System.out.println(record.getType());
//                        System.out.println(record.getLocation());
//                        System.out.println(record.getMetadata());
//                    }
//                }
//                System.out.println("--------------------");
//            });
//        });

        LookupStrategy lookupStrategy = new RadomRobinLookupStrategy();

        vertx.setPeriodic(2000, l -> {
            discovery.getRecords(new JsonObject().put("ServiceName", "mysql"), ar -> {
                if (ar.succeeded()) {
                    List<Record> records = ar.result();
                    Record record = lookupStrategy.getRecord(records);
                    if (record != null) {
                        System.out.println(record.getType());
                        System.out.println(record.getLocation());
                        System.out.println(record.getMetadata());
                    }
                }
                System.out.println("--------------------");
            });
        });
        discovery.close();
    }

}
