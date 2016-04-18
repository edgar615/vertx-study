package com.edgar.vertx.eventbus.pointtopoint;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Receiver extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runClusteredExample(Receiver.class);
    }

    @Override
    public void start() throws Exception {
        EventBus eb = vertx.eventBus();

        eb.consumer("ping-address", message -> {
            System.out.println("Received message: " + message.body());
// Now send back reply
            message.reply("pong!");
        }).completionHandler(ar -> {
          if (ar.succeeded()) {
            System.out.println("Receiver ready!");
          } else {
            System.out.println("Receiver failed!");
          }
        });

    }
}
