package com.edgar.util.vertx.task;

import io.vertx.core.Future;

/**
 * Created by edgar on 16-4-28.
 */
public class ConsumerTest {
  public static void main(String[] args) {
    Future<String> future = Future.future();
    Task<String> task = new Task<>(future)
            .map("length", s -> s.length())
            .onFailure("length error", t -> System.out.println("error:" + t.getMessage()))
            .andThen("output length", i -> System.out.println("the length:" + i))
            .map("length * 10", i -> i * 10)
            .andThen("output length * 10", i -> System.out.println("the length * 10:" + i))
            .flatMap("faltMap", (i, f) -> {
              f.complete(i + "bi");
            });
    future.complete("Hello world");
  }
}
