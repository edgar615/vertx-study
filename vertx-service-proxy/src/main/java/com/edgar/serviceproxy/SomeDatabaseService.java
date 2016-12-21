package com.edgar.serviceproxy;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Return types
 * <p>
 * Must be one of
 * <pre>
 * void
 * @Fluent and return reference to the service (this):
 * </pre>
 * This is because methods must not block and it’s not possible to return a result immediately
 * without blocking if the service is remote.
 * <p>
 * Parameter types
 * <p>
 * Let JSON = JsonObject | JsonArray Let PRIMITIVE = Any primitive type or boxed primitive type
 * <p>
 * Parameters can be any of:
 * <p>
 * JSON
 * <p>
 * PRIMITIVE
 * <p>
 * List<JSON>
 * <p>
 * List<PRIMITIVE>
 * <p>
 * Set<JSON>
 * <p>
 * Set<PRIMITIVE>
 * <p>
 * Map<String, JSON>
 * <p>
 * Map<String, PRIMITIVE>
 * <p>
 * Any Enum type
 * <p>
 * Any class annotated with @DataObject
 * <p>
 * <p>
 * <p>
 * If an asynchronous result is required a last parameter of type Handler<AsyncResult<R>> can be
 * provided.
 * <p>
 * R can be any of:
 * <p>
 * JSON
 * <p>
 * PRIMITIVE
 * <p>
 * List<JSON>
 * <p>
 * List<PRIMITIVE>
 * <p>
 * Set<JSON>
 * <p>
 * Set<PRIMITIVE>
 * <p>
 * Any Enum type
 * <p>
 * Any class annotated with @DataObject
 * <p>
 * Another proxy
 */
@ProxyGen
public interface SomeDatabaseService {

  void save(String collection, JsonObject document, Handler<AsyncResult<Void>> result);

  @Fluent
  SomeDatabaseService foo(String collection, JsonObject document,
                          Handler<AsyncResult<Void>> result);

  static SomeDatabaseService createProxy(Vertx vertx, String address) {
//    return new SomeDatabaseServiceVertxEBProxy(vertx, address);
    return null;
  }
}