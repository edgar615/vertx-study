package com.edgar.vertx.tcp.share;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(Server.class);
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(
                "com.edgar.vertx.tcp.share.TcpServerVerticle",
                new DeploymentOptions().setInstances(2)
        );
    }
}