package com.edgar.vertx.service.discovery.state;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
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

        Map<String, String> map = new HashMap<>();
        vertx.setPeriodic(3000, l -> {
            Record httpRecord = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
            discovery.publish(httpRecord, ar -> {
                if (ar.succeeded()) {
                    Record publishedRecord = ar.result();
                    map.put("id", httpRecord.getRegistration());
                    System.out.println(httpRecord.getRegistration());
                    System.out.println(publishedRecord.getStatus());
                } else {

                }
            });
        });

        vertx.setTimer(500, l -> {
            vertx.setPeriodic(3000, l2 -> {
                String id = map.get("id");
                System.out.println(id);
                discovery.unpublish(id, ar -> {
                    if (ar.succeeded()) {
                        System.out.println("unpublish");
                    } else {
                        System.out.println("unpublish failed");
                        ar.cause().printStackTrace();
                    }
                });
            });
        });

        vertx.eventBus().consumer("vertx.discovery.announce", msg -> {
            System.out.println(msg.body());
        });

        vertx.eventBus().consumer("vertx.discovery.usage", msg -> {
            System.out.println(msg.body());
        });
        discovery.close();
    }
}
