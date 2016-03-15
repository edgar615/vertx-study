package com.edgar.vertx.worker;

import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/8/31.
 */
public class WorkerVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("[Worker] Starting in " + Thread.currentThread().getName());
        vertx.eventBus().consumer("sample.data", message -> {
            System.out.println("[Worker] Consuming data in " + Thread.currentThread().getName());
            String body = message.body().toString();
            message.reply(body.toUpperCase());
        });
    }
}
