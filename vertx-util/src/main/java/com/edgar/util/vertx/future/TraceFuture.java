package com.edgar.util.vertx.future;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * 带日志记录的Future.
 *
 * @author Edgar  Date 2016/5/6
 */
public interface TraceFuture<T> extends Future<T>, Handler<AsyncResult<T>> {

  long startMills();

  long endMills();

  long elapsed();

  static <T> TraceFuture<T> create() {
    return new TraceFutureImpl<>();
  }

  static <T> TraceFuture<T> create(Future<T> future) {
    return new TraceFutureImpl<>(future);
  }
}
