package com.edgar.util.vertx.task;

import io.vertx.core.Future;

/**
 * Created by edgar on 16-4-28.
 */
public class ErrorTest {
  public static void main(String[] args) {
    Future<String> future = Future.future();
    Task<Integer> task = new Task<>(future)
            .map("length", s -> s.length())
            .andThen("output length", i -> System.out.println("the length:" + i))
            .andThen("output length", i -> {
              throw new RuntimeException("oh ho");
            })
            .onFailure("length error", t -> System.out.println("length error:" + t.getMessage()))
            .map("length * 10", i -> i * 10)
            .andThen("output length * 10", i -> System.out.println("the length * 10:" + i))
            .andThen("output2 length * 10", i -> {
              throw new RuntimeException("oh no");
            }).onFailure("length * 10 error", t -> System.out.println("length * 10 error:" + t
                    .getMessage()));
    future.complete("Hello world");
  }
}
