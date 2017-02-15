package com.edgar.vertx.future;

import io.vertx.core.Future;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class FutureExample3 {
  public static void main(String[] args) {

    Future<String> future = Future.future();
    Future<String> future1 = Future.future();
//    future.compose(s -> System.out.println(s), future1);
    future1.setHandler(ar -> {
      if (ar.succeeded()) {
        System.out.println("future1:" + ar.result());
      } else {
        System.out.println("errr");
      }
    });
    future.setHandler(future1.completer());
    future.complete("haha");
//    future.fail("oh no");
  }
}
