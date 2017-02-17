package com.edgar.vertx.service.discovery.lookup;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.servicediscovery.ServiceDiscovery;

/**
 * Created by edgar on 16-6-29.
 */
public class LookupVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", LookupVerticle.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
//        discovery.getRecords(r -> true, ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords((JsonObject) null, ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(r -> r.getType().equals("http-endpoint"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(new JsonObject().put("type", "http-endpoint"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//
//        discovery.getRecords(r -> r.getName().equals("some-rest-api1"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(new JsonObject().put("name", "some-rest-api1"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(r -> "red".equals(r.getMetadata().getString("color")), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(new JsonObject().put("color", "red"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(r -> r.getMetadata().containsKey("color"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
//
//        discovery.getRecords(new JsonObject().put("color", "*"), ar -> {
//            List<Record> records = ar.result();
//            for (Record record : records) {
//                System.out.println(record.getName() + ":" + record.getMetadata());
//            }
//        });
    discovery.close();
  }
}
