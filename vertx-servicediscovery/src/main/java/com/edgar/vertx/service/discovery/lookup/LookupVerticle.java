package com.edgar.vertx.service.discovery.lookup;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;

import java.util.List;

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

        discovery.getRecords(r -> true, ar -> {
            List<Record> records = ar.result();
            System.out.println("all");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });

        discovery.getRecords(r -> r.getType().equals("http-endpoint"), ar -> {
            List<Record> records = ar.result();
            System.out.println("http-endpoint");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });


        discovery.getRecords(r -> r.getName().equals("some-rest-api1"), ar -> {
            List<Record> records = ar.result();
            System.out.println("some-rest-api1");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });

        discovery.getRecords(new JsonObject().put("name", "some-rest-api1"), ar -> {
            List<Record> records = ar.result();
            System.out.println("json name some-rest-api1");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });

        discovery.getRecords(r -> "red".equals(r.getMetadata().getString("color")), ar -> {
            List<Record> records = ar.result();
            System.out.println("red api");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });

        discovery.getRecords(new JsonObject().put("color", "red"), ar -> {
            List<Record> records = ar.result();
            System.out.println("json red api");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });

        discovery.getRecords(r -> r.getMetadata().containsKey("color"), ar -> {
            List<Record> records = ar.result();
            System.out.println("has color api");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });

        discovery.getRecords(new JsonObject().put("color", "*"), ar -> {
            List<Record> records = ar.result();
            System.out.println("json has color api");
            for (Record record : records) {
                System.out.println("-" + record.getName());
            }
        });
        discovery.close();
    }
}
