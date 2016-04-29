package com.edgar.util.vertx.task;

import io.vertx.core.Future;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class Task<T> {

  private final Future<T> future;

  public Task(Future<T> future) {
    this.future = future;
  }

//  public static <T> Task<T> create() {
//    return new Task<>(Future.<T>future());
//  }

  public <R> Task<R> map(String desc, Function<T, R> function) {
    Future<R> rFuture = Future.future();
    TransformHandler<T, R> handler = new TransformHandler<>(desc, rFuture,
                                                            function, null);
    future.setHandler(handler);
    return new Task<>(rFuture);
  }

  public Task<T> andThen(String desc, Consumer<T> consumer) {
    return map(desc, t -> {
      consumer.accept(t);
      return t;
    });
  }

  public <R> Task<R> flatMap(String desc, BiConsumer<T, Future<R>> consumer) {
    Future<R> rFuture = Future.future();
    future.setHandler(new FuturePropagator(desc, rFuture, consumer));
    return new Task<>(rFuture);
  }

  public Task<T> onFailure(String desc, Consumer<Throwable> consumer) {
    Future<T> rFuture = Future.future();
    TransformHandler<T, T> handler = new TransformHandler<>(desc, rFuture,
                                                            t -> t,
                                                            consumer);
    future.setHandler(handler);
    return new Task<>(rFuture);
  }

  public static <T> Task<T> create(Consumer<Future<T>> consumer) {
    Future<T> future = Future.<T>future();
    Task<T> task = new Task<>(future);
    consumer.accept(future);
    return task;
  }
}
