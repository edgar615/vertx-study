package com.edgar.vertx.worker;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * Created by Administrator on 2015/8/31.
 */
public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(MainVerticle.class);
    }

    @Override
    public void start() throws Exception {
        System.out.println("[Main] Running in " + Thread.currentThread().getName());
        vertx.deployVerticle("com.edgar.vertx.worker.WorkerVerticle", new DeploymentOptions().setWorker(true));

        vertx.eventBus().send("sample.data",
                "hello vert.x", res -> {
                    if (res.succeeded()) {
                        System.out.println("[Main] Receiving reply ' " + res.result().body().toString()
                                + "' in " + Thread.currentThread().getName());
                    }
                });
    }
}
