package com.edgar.vertx.eventbus.pointtopoint;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Sender extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runClusteredExample(Sender.class);
    }

    @Override
    public void start() throws Exception {
        EventBus eb = vertx.eventBus();
        vertx.setPeriodic(1000, l -> {
           eb.send("ping-address", "ping!", reply -> {
               if (reply.succeeded()) {
                   System.out.println("Received reply " + reply.result().body());
               } else {
                   System.out.println("No reply");
               }
           });
        });
    }
}
