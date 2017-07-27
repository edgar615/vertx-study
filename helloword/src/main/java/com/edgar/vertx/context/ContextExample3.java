package com.edgar.vertx.context;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Edgar on 2017/5/3.
 *
 * @author Edgar  Date 2017/5/3
 */
public class ContextExample3 extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ContextExample3.class.getName());
  }

  @Override
  public void start() throws Exception {
    Context context = Vertx.currentContext();
    System.out.println("Running with context : " + Vertx.currentContext());
// Our blocking action
    System.out.println(Thread.currentThread());
    final CompletableFuture<Integer> toComplete = new CompletableFuture<>();
    // delay future completion by 500 ms
    final String threadName = Thread.currentThread().getName();
    System.out.println(threadName);
    System.out.println(Vertx.currentContext());
    toComplete.complete(100);
    vertx.getOrCreateContext().exceptionHandler(throwable -> {
      throwable.printStackTrace();
    });
    toComplete.thenRun(() -> {
      System.out.println(Thread.currentThread());
      System.out.println("Current context : " + Vertx.currentContext());
      throw new RuntimeException("a");
//            async.complete();
    });
  }
}
