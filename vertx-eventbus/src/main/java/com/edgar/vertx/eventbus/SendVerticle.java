package com.edgar.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;

public class SendVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.deployVerticle("com.edgar.vertx.eventbus.ConsumerVerticle", new DeploymentOptions()
            .setInstances(2), ar -> {
      if (ar.succeeded()) {
        vertx.eventBus()
                .send("com.edgar.vertx.test", "hello world", new DeliveryOptions().setSendTimeout
                              (1000),
                      (AsyncResult<Message<String>>
                                                                            ar2)
                        -> {
                  if (ar2.succeeded()) {
                    System.out.println(ar2.result().body());
                  } else {
                    System.err.println(ar2.cause().getMessage());
                  }
                });
      }
    });

  }
}