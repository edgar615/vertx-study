package com.edgar.vertx.helloword;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 2016/9/29.
 *
 * @author Edgar  Date 2016/9/29
 */
@RunWith(VertxUnitRunner.class)
public class CompositeFutureTest2 {

  Vertx vertx;
  @Before
  public void setUp(TestContext testContext) {
    vertx = Vertx.vertx();
    vertx.createHttpServer().requestHandler(req -> {
      if (req.path().startsWith("/foo1")) {
        req.response().end("foo1");
      }
      if (req.path().startsWith("/foo2")) {
        vertx.setTimer(5000, l -> {
          req.response().end("foo2");
        });
      }
    }).listen(8080, testContext.asyncAssertSuccess());
  }

  @Test
  public void test(TestContext testContext) {
    Async async = testContext.async();


    Future<String> future1 = Future.future();
    Future<String> future2 = Future.future();
    List<Future> futures = new ArrayList<>();
    futures.add(future1);
    futures.add(future2);
    CompositeFuture.all(futures).setHandler(ar -> {
      if (ar.succeeded()) {
        CompositeFuture f = ar.result();
        System.out.println(f.result(0).toString());
        System.out.println(f.result(1).toString());
        async.complete();
      } else {
        System.err.println("error" + ar.cause().getMessage());
        async.complete();
      }
    });
    vertx.createHttpClient().get(8080, "localhost", "/foo1", response -> {
      response.bodyHandler(body -> {
        future1.complete(body.toString());
      });
    }).end();

    vertx.createHttpClient().get(8080, "localhost", "/foo2", response -> {
      response.bodyHandler(body -> {
        future2.complete(body.toString());
      });
    }).end();
  }
}
