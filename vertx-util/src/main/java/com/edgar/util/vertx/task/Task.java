package com.edgar.util.vertx.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class Task<T> {

  private final Vertx vertx;

  private final Future<T> future;

  public Task(Vertx vertx, Future<T> future) {
    this.vertx = vertx;
    this.future = future;
  }

  public void onNext(Consumer<T> successHandler, Consumer<Throwable> failureHandler) {
    if (future.succeeded()) {
      successHandler.accept(future.result());
    } else {
      failureHandler.accept(future.cause());
    }
  }

  public void complete(T result) {
    future.complete(result);
  }

//  public <R> Task<R> map(Function<T, R> function) {
//    R r = function.apply(future.result());
//    return new Task<>(vertx, );
//  }
}
