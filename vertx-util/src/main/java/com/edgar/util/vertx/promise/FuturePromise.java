package com.edgar.util.vertx.promise;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class FuturePromise<T> {
  private final Future<T> future;

  public FuturePromise(Future<T> future) {this.future = future;}

  public void then(JsonObject context, Handler<Future<T>> handler) {

  }
}
