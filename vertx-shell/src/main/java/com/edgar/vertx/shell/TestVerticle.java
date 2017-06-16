package com.edgar.vertx.shell;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2017/6/16.
 *
 * @author Edgar  Date 2017/6/16
 */
public class TestVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    new Launcher().execute("run", TestVerticle.class.getName(),
                           "--cluster");
  }

  @Override
  public void start() throws Exception {
    System.out.println("start");
  }

  @Override
  public void stop() throws Exception {
    System.out.println("stop");
  }
}
