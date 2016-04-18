package com.edgar.vertx.eventbus.pubsub;

import com.edgar.util.vertx.runner.Runner;
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
           eb.publish("news-feed", "some data");
        });
    }
}
