package com.edgar.vertx.future;

import io.vertx.core.Future;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class FutureExample2 {
  public static void main(String[] args) {

    Future<String> future = Future.future();
    Future<Integer> future1 = Future.future();
    future.compose(s -> System.out.println(s), future1);
    future1.setHandler(ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
      } else {
        System.out.println("errr");
      }
    });
//    future.complete("haha");
    future.fail("oh no");
  }
}
