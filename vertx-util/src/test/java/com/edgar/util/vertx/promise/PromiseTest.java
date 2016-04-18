package com.edgar.util.vertx.promise;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
@RunWith(VertxUnitRunner.class)
public class PromiseTest {

  Vertx vertx;

  HttpServer server;

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();
    server = vertx.createHttpServer().requestHandler(req -> {
      String url = req.path();
      if (url.equals("/foo") && req.method() == HttpMethod.GET) {
        System.out.println(req.path());
        System.out.println(req.absoluteURI());
        req.response().putHeader("Content-Type", "application/json")
                .end(new JsonObject()
                             .put("foo", "bar")
                             .encode());
      }
      if (url.equals("/foo/array") && req.method() == HttpMethod.GET) {
        req.response().putHeader("Content-Type", "application/json")
                .end(new JsonArray()
                             .add(new JsonObject()
                                          .put("foo", "bar")
                                          .encode()).encode());
      }
      if (url.equals("/foo") && req.method() == HttpMethod.DELETE) {
        req.response().putHeader("Content-Type", "application/json")
                .end(new JsonObject()
                             .put("foo", "bar")
                             .encode());
      }
      if (url.equals("/foo") && req.method() == HttpMethod.POST) {
        req.response().putHeader("Content-Type", "application/json");
        req.bodyHandler(body -> req.response().end(body));
      }
      if (url.equals("/foo") && req.method() == HttpMethod.PUT) {
        req.response().putHeader("Content-Type", "application/json");
        req.bodyHandler(body -> req.response().end(body));
      }
    }).listen(8080, context.asyncAssertSuccess());
  }

  @Test
  public void testPromiseDone(TestContext testContext) {
    Async async = testContext.async();
    Promise.newInstance(vertx)
            .then((context, onResult) -> {
              context.put("result", "some text to share");
              onResult.accept(true);
            }).then((context, onResult) -> onResult.accept(context.containsKey("result")))
// optional exception handler, when a promise calls onResult.accept(false) or a callback throws
// an exception
            .except((context) -> testContext.fail())
// optional completion handler called when all callbacks have run and succeeded
            .done((context) -> {
              System.out.println("Success: " + context.encode());
              testContext.assertEquals("some text to share", context.getString("result"));
              async.complete();
            })
// optionally set a timeout in ms for the callback chain to complete in
            .timeout(3000)
// you are required to call this once and only once to make the promise chain begin to evaluate
            .eval();
  }

  @Test
  public void testPromiseFailed(TestContext testContext) {
    Async async = testContext.async();
    Promise.newInstance(vertx)
            .then((context, onResult) -> {
              context.put("result", "some text to share");
              onResult.accept(false);
            }).then((context, onResult) -> onResult.accept(context.containsKey("result")))
            .except((context) -> {
              System.out.println(context);
              async.complete();
            })
            .done((context) -> testContext.fail())
            .timeout(3000)
            .eval();
  }

  @Test
  public void testPromiseEx(TestContext testContext) {
    Async async = testContext.async();
    Promise.newInstance(vertx)
            .then((context, onResult) -> {
              context.put("result", "some text to share");
              onResult.accept(true);
            }).then((context, onResult) -> {throw new RuntimeException();})
            .except((context) -> {
              System.out.println(context.getString("failure"));
              async.complete();
            })
            .done((context) -> testContext.fail())
            .timeout(3000)
            .eval();
  }
}
