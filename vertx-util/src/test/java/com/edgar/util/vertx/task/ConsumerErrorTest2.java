package com.edgar.util.vertx.task;

import io.vertx.core.Future;

/**
 * Created by edgar on 16-4-28.
 */
public class ConsumerErrorTest2 {
  public static void main(String[] args) {
    Future<String> future = Future.future();
    Task<String> task = new Task<>(future)
            .map("length", s -> s.length())
            .andThen("output length", i -> System.out.println("the length:" + i))
            .onFailure("length error", t -> System.out.println("length error:" + t.getMessage()))
            .map("length * 10", i -> i * 10)
            .andThen("output length * 10", i -> System.out.println("the length * 10:" + i))
            .flatMap("faltMap", (Integer i, Future<String> f) -> {
//              f.complete(i + "bi");
              f.fail("hoho");
            }).onFailure("flatMap error", t -> System.out.println("flatMap error:" + t.getMessage
                    ()));
    future.complete("Hello world");
  }
}
