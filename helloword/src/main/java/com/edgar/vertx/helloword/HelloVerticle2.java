package com.edgar.vertx.helloword;

import io.vertx.core.AbstractVerticle;

/**
 * Created by edgar on 16-3-4.
 */
public class HelloVerticle2 extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.runOnContext((event) -> {
      System.out.println(context);
      System.out.println(vertx.getOrCreateContext());
    });
  }
}
