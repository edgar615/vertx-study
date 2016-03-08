package com.edgar.vertx.helloword;

import io.vertx.core.AbstractVerticle;

/**
 * Created by edgar on 16-3-4.
 */
public class PeriodServerVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(10000, l -> System.out.println("timer fired!" + l));
    vertx.setTimer(1000, l -> System.out.println("timer fired!" + l));
  }
}
