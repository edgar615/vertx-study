package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.function.BiConsumer;

/**
 * 传播任务，在一个任务执行完成后运行.
 * 构造函数接收两个参数Task<R> task, BiConsumer<T, Task<R>> consumer,
 * 在上一个任务完成之后可以通过consumer.accept()方法中，通过调用task.complete或者task.fail方法来结束任务，将任务继续向下执行。
 *
 * @param <T>
 * @param <R>
 */
class ComposeHandler<T, R> implements Handler<AsyncResult<T>> {
  private final Task<R> task;

  private final BiConsumer<T, Task<R>> consumer;

  ComposeHandler(Task<R> task, BiConsumer<T, Task<R>> consumer) {
    this.task = task;
    this.consumer = consumer;
  }

  @Override
  public void handle(AsyncResult<T> ar) {
    Throwable throwable = ar.cause();
    if (ar.succeeded()) {
      try {
        consumer.accept(ar.result(), task);
      } catch (Exception e) {
        throwable = e;
      }
    }
    if (throwable != null) {
      task.fail(throwable);
    }
  }
}