package com.edgar.vertx.service.discovery.lookup;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar on 16-6-29.
 */
public class HttpServiceVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", HttpServiceVerticle.class.getName(), "--cluster");
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        Record httpRecord = HttpEndpoint.createRecord("some-rest-api1", "localhost", 8080, "/api");
        discovery.publish(httpRecord, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getRegistration());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });

        Record httpRecord2 = HttpEndpoint.createRecord("some-rest-api2", "localhost", 8080, "/api", new JsonObject().put("color", "red"));
        discovery.publish(httpRecord2, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getRegistration());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });

        Record httpRecord3 = HttpEndpoint.createRecord("some-rest-api3", "localhost", 8080, "/api", new JsonObject().put("color", "white"));
        discovery.publish(httpRecord3, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getRegistration());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });

        Record httpRecord4 = HttpEndpoint.createRecord("some-rest-api1", "localhost", 8080, "/api", new JsonObject().put("num", 1));
        discovery.publish(httpRecord4, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getRegistration());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });

        discovery.close();
    }
}
