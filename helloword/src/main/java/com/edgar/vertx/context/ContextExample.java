package com.edgar.vertx.context;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/5/3.
 *
 * @author Edgar  Date 2017/5/3
 */
public class ContextExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ContextExample.class.getName());
  }

  @Override
  public void start() throws Exception {
    System.out.println(vertx.getOrCreateContext().getInstanceCount());
    vertx.deployVerticle(ContextThreadExample.class.getName(), ar -> {
      System.out.println(vertx.getOrCreateContext().getInstanceCount());
    });
    System.out.println(Thread.currentThread().getId());
    vertx.runOnContext(v -> {
      System.out.println(Thread.currentThread().getId());
    });

  }
}
