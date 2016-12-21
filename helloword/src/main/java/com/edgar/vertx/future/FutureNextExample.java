package com.edgar.vertx.future;

import io.vertx.core.Future;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class FutureNextExample {
  public static void main(String[] args) {

    Future<String> future1 = Future.future();
    future1.setHandler(ar -> {
      if (ar.succeeded()) {
        System.out.println("future1");
      } else {
        System.out.println("future1 errr");
      }
    });
    future1.complete("hello world");

    future1.compose(s -> Future.succeededFuture(s.length()))
            .setHandler(ar -> {
              if (ar.succeeded()) {
                System.out.println("future2");
              } else {
                System.out.println("future2 errr");
              }
            });

    //第一个future失败
    Future<String> future3 = Future.future();
    future3.setHandler(ar -> {
      if (ar.succeeded()) {
        System.out.println("future3");
      } else {
        System.out.println("future3 errr");
      }
    });
    future3.fail("hello world");

    future3.compose(s -> Future.succeededFuture(s.length()))
            .setHandler(ar -> {
              if (ar.succeeded()) {
                System.out.println("future4");
              } else {
                System.out.println("future4 errr");
              }
            });

    //第二个future失败
    Future<String> future5 = Future.future();
    future5.setHandler(ar -> {
      if (ar.succeeded()) {
        System.out.println("future5");
      } else {
        System.out.println("future5 errr");
      }
    });
    future5.complete("hello world");

    future5.compose(s -> Future.succeededFuture(Integer.parseInt(s)))
            .setHandler(ar -> {
              if (ar.succeeded()) {
                System.out.println("future6");
              } else {
                System.out.println("future6 errr");
              }
            });
  }


}
