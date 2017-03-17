package com.edgar.vertx.timer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/3/17.
 *
 * @author Edgar  Date 2017/3/17
 */
public class TimerVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(TimerVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    long timerID = vertx.setTimer(1000, id -> {
      System.out.println(System.currentTimeMillis() + ": And one second later this is printed");
    });

    System.out.println(System.currentTimeMillis() + ": First this is printed");
  }
}
