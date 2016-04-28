package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by edgar on 16-4-28.
 */
public class TransformHandler<T, R> implements Handler<AsyncResult<T>> {

  private final Future<R> dest;
  private final Function<T, R> function;
  private final Consumer<Throwable> consumer;

  public TransformHandler(Future<R> dest, Function<T, R> function, Consumer<Throwable> consumer) {
    this.dest = dest;
    this.function = function;
    this.consumer = consumer;
  }

  @Override
  public void handle(AsyncResult<T> future) {
    if (future.succeeded()) {
      dest.complete(function.apply(future.result()));
    } else {
      if (consumer != null) {
        consumer.accept(future.cause());
      }
      dest.fail(future.cause());
    }
  }
}
