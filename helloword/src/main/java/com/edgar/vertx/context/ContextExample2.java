package com.edgar.vertx.context;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/5/3.
 *
 * @author Edgar  Date 2017/5/3
 */
public class ContextExample2 extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ContextExample2.class.getName());
  }

  @Override
  public void start() throws Exception {
    Context context = Vertx.currentContext();
    System.out.println("Running with context : " + Vertx.currentContext());
// Our blocking action
    System.out.println(Thread.currentThread());
    Thread thread = new Thread() {
      public void run() {
        // No context here!
        System.out.println(Thread.currentThread());
        System.out.println("Current context : " + Vertx.currentContext());
        context.runOnContext(v -> {
          // Runs on the same context
          System.out.println(Thread.currentThread());
          System.out.println("Runs on the original context : " + Vertx.currentContext());
        });
      }
    };
//
    thread.start();
  }
}
