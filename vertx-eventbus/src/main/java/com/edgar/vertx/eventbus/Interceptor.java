package com.edgar.vertx.eventbus;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Interceptor {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();

      eb.addInterceptor(sc -> {
        System.out.println(sc.message().body());
        System.out.println(sc.send());
        sc.next();
      });

        eb.consumer("news.uk.sport", message -> {
            System.out.println("header: " + message.headers());
            System.out.println("I have received a message: " + message.body());
        });

        DeliveryOptions options = new DeliveryOptions();
        options.addHeader("some-header", "some-value");
        eb.publish("news.uk.sport", "Yay! Someone kicked a ball", options);

    }
}
