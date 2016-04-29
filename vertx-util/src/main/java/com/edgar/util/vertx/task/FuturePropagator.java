package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.BiConsumer;

/**
 * Created by Edgar on 2016/4/29.
 *
 * @author Edgar  Date 2016/4/29
 */
public class FuturePropagator<T, R> implements Handler<AsyncResult<T>> {

  private final String desc;

  private final Future<R> future;

  private final BiConsumer<T, Future<R>> consumer;

  public FuturePropagator(String desc, Future<R> future, BiConsumer<T, Future<R>> consumer) {
    this.desc = desc;
    this.future = future;
    this.consumer = consumer;
  }

  @Override
  public void handle(AsyncResult<T> ar) {
    Throwable throwable = ar.cause();
    if (ar.succeeded()) {
      try {
        consumer.accept(ar.result(), future);
      } catch (Exception e) {
        throwable = e;
      }
    }
    if (throwable != null) {
      future.fail(throwable);
    }
  }
}
