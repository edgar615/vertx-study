package com.edgar.vertx.eventbus;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * Created by Administrator on 2015/8/31.
 */
public class EventBusServer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        eb.consumer("news.uk.sport", message -> {
            System.out.println("I have received a message: " + message.body());
            //Replying to messages
//            Sometimes after you send a message you want to receive a reply from the recipient. This is known as the request-response pattern.
            message.reply("how interesting!");
        }).completionHandler(res -> {
            if (res.succeeded()) {
                System.out.println("The handler registration has reached all nodes");
            } else {
                System.out.println("Un-registration failed!");
            }
        });//If you want to be notified when this has completed, you can register a completion handler on the MessageConsumer object.

        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
        consumer.handler(message -> {
            System.out.println("I also have received a message: " + message.body());
        });

        //Publishing messages
        //That message will then be delivered to all handlers registered against the address news.uk.sport.
        eb.publish("news.uk.sport", "Yay! Someone kicked a ball");

        //Sending messages
        //Sending a message will result in only one handler registered at the address receiving the message. This is the point to point messaging pattern. The handler is chosen in a non-strict round-robin fashion.
        eb.send("news.uk.sport", "send! Someone kicked a ball");

        //Un-registering Handlers
        consumer.unregister(res -> {
            if (res.succeeded()) {
                System.out.println("The handler un-registration has reached all nodes");
            } else {
                System.out.println("Un-registration failed!");
            }
        });

        eb.publish("news.uk.sport", "Yay! Someone kicked a ball");

        //Setting headers on messages
        DeliveryOptions options = new DeliveryOptions();
        options.addHeader("some-header", "some-value");
        eb.send("news.uk.sport", "Yay! Someone kicked a ball", options);

        eb.send("news.uk.sport", "Yay! Someone kicked a ball across a patch of grass", ar -> {
            if (ar.succeeded()) {
                System.out.println("Received reply: " + ar.result().body());
            }
        });
    }
}
