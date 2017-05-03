package com.edgar.vertx.context;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/5/3.
 *
 * @author Edgar  Date 2017/5/3
 */
public class ContextThreadExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ContextThreadExample.class.getName());
  }

  @Override
  public void start() throws Exception {
    vertx.eventBus().consumer("test", msg -> {
      System.out.println(Thread.currentThread().getId());
      msg.reply("pong");
    });

   vertx.setPeriodic(1000, l -> {
     vertx.eventBus().send("test", "ping", msg-> {
       System.out.println(Thread.currentThread().getId());
     });
   });
  }
}
