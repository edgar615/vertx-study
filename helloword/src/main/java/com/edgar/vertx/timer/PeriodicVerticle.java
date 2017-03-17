package com.edgar.vertx.timer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/3/17.
 *
 * @author Edgar  Date 2017/3/17
 */
public class PeriodicVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(PeriodicVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    long timerID = vertx.setPeriodic(1000, id -> {
      System.out.println(System.currentTimeMillis() + ": And every second this is printed");
    });

    System.out.println(System.currentTimeMillis() + ": First this is printed");
  }
}
