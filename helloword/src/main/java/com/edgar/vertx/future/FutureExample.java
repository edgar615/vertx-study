package com.edgar.vertx.future;

import io.vertx.core.Future;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class FutureExample {
  public static void main(String[] args) {

    Future<String> future = Future.future();
    future.setHandler(ar -> {
      if (ar.succeeded()) {
        throw new RuntimeException();
      } else {
        System.out.println("errr");
      }
    });
    future.complete("haha");
  }
}
