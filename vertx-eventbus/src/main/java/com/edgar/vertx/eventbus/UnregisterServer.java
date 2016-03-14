package com.edgar.vertx.eventbus;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/31.
 */
public class UnregisterServer {
    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
        consumer.handler(message -> {
            System.out.println("I also have received a message: " + message.body());
        });

        //Publishing messages
        //That message will then be delivered to all handlers registered against the address news.uk.sport.
        eb.publish("news.uk.sport", "Yay! Someone kicked a ball");


        TimeUnit.SECONDS.sleep(5);
        //Un-registering Handlers
        consumer.unregister(res -> {
            if (res.succeeded()) {
                System.out.println("The handler un-registration has reached all nodes");
            } else {
                System.out.println("Un-registration failed!");
            }
        });

        eb.publish("news.uk.sport", "Yay! Someone kicked a ball");
    }
}
