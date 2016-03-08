package com.edgar.vertx.helloword;

import io.vertx.core.AbstractVerticle;

/**
 * Created by edgar on 16-3-4.
 */
public class HelloVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

//    vertx.deployVerticle("com.edgar.vertx.helloword.HelloVerticle2");

    vertx.runOnContext((event) -> {
      System.out.println(context);
      System.out.println(vertx.getOrCreateContext());
    });
  }
}
