package com.edgar.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.Message;

public class PublishVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.deployVerticle("com.edgar.vertx.eventbus.ConsumerVerticle", new DeploymentOptions()
            .setInstances(2), ar -> {
      if (ar.succeeded()) {
        vertx.eventBus()
                .publish("com.edgar.vertx.test", "hello world");
      }
    });
  }
}