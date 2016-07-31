package com.edgar.util.vertx.future;

import io.vertx.core.Future;

import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2016/5/6.
 *
 * @author Edgar  Date 2016/5/6
 */
public class FutureTest {
  public static void main(String[] args) throws InterruptedException {
    Future<Integer> future = Future.future();
    future.setHandler(ar -> {
      System.out.println(ar.succeeded());
    });
    future.setHandler(ar -> {
      System.out.println(ar.succeeded());
    });
      future.complete(1);

  }
}
