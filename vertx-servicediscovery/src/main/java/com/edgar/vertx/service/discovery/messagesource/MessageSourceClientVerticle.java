package com.edgar.vertx.service.discovery.messagesource;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;

/**
 * Created by edgar on 16-6-29.
 */
public class MessageSourceClientVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", MessageSourceClientVerticle.class.getName(), "--cluster");
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        discovery.getRecord(new JsonObject().put("name", "some-message-source-service"), ar -> {
            Record record = ar.result();
            System.out.println(record.getType());
            System.out.println(record.getLocation());
            ServiceReference reference = discovery.getReference(record);
            MessageConsumer<JsonObject> messageConsumer = reference.get();
            System.out.println(messageConsumer.address());
//            vertx.eventBus().consumer("some-address", msg -> {
//                System.out.println(msg.body());
//            });
            messageConsumer.handler(msg -> {
                try {
                    JsonObject payload = msg.body();
                    System.out.println(payload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
//            vertx.setPeriodic(1000, l -> {
//                System.out.println("publish");
//                vertx.eventBus().publish(messageConsumer.address(), new JsonObject().put("foo", "bar"));
//            });
            reference.release();
        });
        discovery.close();
    }
}
