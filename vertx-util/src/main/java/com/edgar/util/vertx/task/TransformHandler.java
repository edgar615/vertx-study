package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by edgar on 16-4-28.
 */
class TransformHandler<T, R> implements Handler<AsyncResult<T>> {

  private final Future<R> future;

  private final Function<T, R> function;

  private final Consumer<Throwable> failureHandler;

  private final String desc;

  public TransformHandler(String desc, Future<R> future, Function<T, R> function,
                          Consumer<Throwable> failureHandler) {
    this.desc = desc;
    this.future = future;
    this.function = function;
    this.failureHandler = failureHandler;
  }

  @Override
  public void handle(AsyncResult<T> result) {
    Throwable throwable = result.cause();
    if (result.succeeded()) {
      try {
        future.complete(function.apply(result.result()));
      } catch (Exception e) {
        throwable = e;
      }
    }
    if (throwable != null) {
      if (failureHandler != null) {
        failureHandler.accept(throwable);
      }
      future.fail(throwable);
    }
  }
}
