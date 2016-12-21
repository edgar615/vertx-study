package com.edgar.vertx.helloword;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2016/11/23.
 *
 * @author Edgar  Date 2016/11/23
 */
public class MyVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(MyVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    System.out.println("started");
  }

  @Override
  public void stop() throws Exception {
    System.out.println("stopped");
  }
}
