package com.edgar.vertx.eventbus;

import io.vertx.core.AbstractVerticle;

public class ConsumerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.eventBus().<String>consumer("com.edgar.vertx.test", msg -> {
      System.out.println(msg.body());
      msg.reply("reply");
    }).completionHandler(res -> {
      if (res.succeeded()) {
        System.out.println("The handler registration has reached all nodes");
      } else {
        System.out.println("Registration failed!");
      }
    });//When registering a handler on a clustered event bus, it can take some time for the registration to reach all nodes of the cluster.
//    If you want to be notified when this has completed, you can register a completion handler on the MessageConsumer object.

    //To unregister a handler, call unregister.

  }
}