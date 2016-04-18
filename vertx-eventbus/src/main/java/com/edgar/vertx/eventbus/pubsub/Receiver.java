package com.edgar.vertx.eventbus.pubsub;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Receiver extends AbstractVerticle {

    public static void main(String[] args) {
      Runner.runClusteredExample(Receiver.class);
    }

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("news-feed", message -> {
            System.out.println("Received news: " + message.body());
        });
        System.out.println("Ready!");
    }
}
