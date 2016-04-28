package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.function.BiConsumer;
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

  public Future<T> getFuture() {
    return future;
  }

  public Vertx getVertx() {
    return vertx;
  }

  public <R> Task<R> map(Function<T, R> function) {
    Future<R> rFuture = Future.future();
    TransformHandler<T, R> handler = new TransformHandler<>(rFuture, function, null);
    future.setHandler(handler);
    return new Task<>(vertx, rFuture);
  }

  public Task<T> andThen(Consumer<T> consumer) {
    return map(t -> {
      consumer.accept(t);
      return t;
    });
  }

  public <R> Task<R> flatMap(Task<R> task) {
    Future<R> rFuture = Future.future();
    return new Task<>(vertx, rFuture);
  }

  public Task<T> onFailure(Consumer<Throwable> consumer) {
    Future<T> rFuture = Future.future();
    TransformHandler<T, T> handler = new TransformHandler<>(rFuture, null, consumer);
    future.setHandler(handler);
    return new Task<>(vertx, rFuture);
  }

}
