package com.edgar.serviceproxy;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class SomeDatabaseServiceImpl implements SomeDatabaseService {

  @Override
  public void save(String collection, JsonObject document, Handler<AsyncResult<Void>> result) {

  }

  @Override
  public SomeDatabaseService foo(String collection, JsonObject document, Handler<AsyncResult<Void>> result) {
    return this;
  }
}