package com.edgar.vertx.launcher;

import io.vertx.core.AbstractVerticle;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class HelloVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    System.out.println("starting");
  }
}
