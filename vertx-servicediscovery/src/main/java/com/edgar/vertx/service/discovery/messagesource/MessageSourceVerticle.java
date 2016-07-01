package com.edgar.vertx.service.discovery.messagesource;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.concurrent.TimeUnit;

/**
 * Created by edgar on 16-6-29.
 */
public class MessageSourceVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", MessageSourceVerticle.class.getName(), "--cluster");
    }

    @Override
    public void start() throws Exception {
//        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
                .setBackendConfiguration(new JsonObject().put("host", "10.11.0.31").put("key", "records")));;
        Record record = MessageSource.createRecord(
                "some-message-source-service", // The service name
                "some-address" // The event bus address
        );

            vertx.setPeriodic(100, l -> {
                System.out.println("publish");
                vertx.eventBus().publish("some-address", new JsonObject().put("foo", "bar"));
            });

//      record = MessageSource.createRecord(
//              "some-other-message-source-service", // The service name
//              "some-address", // The event bus address
//              "examples.MyData" // The payload type
//      );
//      Record record1 = MessageSource.createRecord(
//              "some-message-source-service", // The service name
//              "some-address", // The event bus address
//              JsonObject.class // The message payload type
//      );
//      Record record2 = MessageSource.createRecord(
//              "some-other-message-source-service", // The service name
//              "some-address", // The event bus address
//              JsonObject.class, // The message payload type
//              new JsonObject().put("some-metadata", "some value")
//      );

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getLocation());
                System.out.println(publishedRecord.getType());
                System.out.println(publishedRecord.getStatus());
            } else {
                ar.cause().printStackTrace();
            }
        });

//        vertx.setPeriodic(1000, l -> {
//            System.out.println("publish");
//            vertx.eventBus().publish("some-address", new JsonObject().put("foo", "bar"));
//        });
        discovery.close();
    }
}
