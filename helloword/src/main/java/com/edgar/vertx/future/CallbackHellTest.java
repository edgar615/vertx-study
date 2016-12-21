package com.edgar.vertx.future;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2016/11/24.
 *
 * @author Edgar  Date 2016/11/24
 */
public class CallbackHellTest {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Future<String> future1 = Future.future();
    future1.setHandler(ar -> {
      // do something
      Future<String> future2 = Future.future();
      future2.setHandler(ar2 -> {
        // do something
        Future<String> future3 = Future.future();
        future3.setHandler(ar3 -> {
          // do something
        });

      });

    });
  }
}
