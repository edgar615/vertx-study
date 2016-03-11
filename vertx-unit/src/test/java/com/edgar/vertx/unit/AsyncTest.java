package com.edgar.vertx.unit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestCompletion;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Edgar on 2016/3/11.
 *
 * @author Edgar  Date 2016/3/11
 */
public class AsyncTest {

  public static void main(String[] args) {
    new AsyncTest().testAsync2();
  }

  @Test
  public void testAsync() {
    TestOptions testOptions = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite testSuite = TestSuite.create("async");
    testSuite.test("my_test_case", context -> {
      Async async = context.async();
      context.assertEquals(1, 1);
      async.complete();
    });
//    Creating an Async object with the async method marks the executed test case as non
// terminated. The test case terminates when the complete method is invoked.
    testSuite.run(testOptions);
  }

  Vertx vertx;

  @Test
  public void testAsync2() {

    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> {
      vertx = Vertx.vertx();
      vertx.createHttpServer().requestHandler(req -> req.response().end("foo"))
              .listen(8080, context.asyncAssertSuccess());
    });
    suite.after(context -> {
      vertx.close(context.asyncAssertSuccess());
    });
// Specifying the test names seems ugly...
    suite.test("some_test1", context -> {
// Send a request and get a response
      Async async = context.async();
      HttpClient client = vertx.createHttpClient();
      client.getNow(8080, "localhost", "/", resp -> {
        resp.bodyHandler(body -> context.assertEquals("foo", body.toString("UTF-8")));
        client.close();
        async.complete();
      });
      async.awaitSuccess();
    });

    suite.run(options);
  }

  @Test
  public void testCountDown() {
//    Async can also be created with an initial count value, it completes when the count-down
// reaches zero using countDown:
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
// Specifying the test names seems ugly...
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      Async async = context.async(2);
      HttpServer server = vertx.createHttpServer();
      server.requestHandler(req -> req.response().end("foo"));
      server.listen(8080, ar -> {
        context.assertTrue(ar.succeeded());
        async.countDown();
      });

      vertx.setTimer(1000, id -> {
        //Calling complete() on an async completes the async as usual, it actually sets the value
        // to 0.
        async.complete();
      });

// Wait until completion of the timer and the http request
      async.awaitSuccess();
    });

    suite.run(options);
  }

  @Test
  public void testAsyncAssert() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      Async async = context.async();
      vertx.deployVerticle("com.edgar.vertx.unit.SomeVerticle", ar -> {
        if (ar.succeeded()) {
          async.complete();
        } else {
          context.fail(ar.cause());
        }
      });
    });

    suite.run(options);
  }

  @Test
  public void testAsyncAssert2() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      vertx.deployVerticle("com.edgar.vertx.unit.SomeVerticle", context.asyncAssertSuccess());
    });

    suite.run(options);
  }

  @Test
  public void testAsyncAssert3() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      AtomicBoolean started = new AtomicBoolean();
      Async async = context.async();
      vertx.deployVerticle(new AbstractVerticle() {
        @Override
        public void start(Future<Void> startFuture) throws Exception {
          started.set(true);
        }
      }, ar -> {
        if (ar.succeeded()) {
          context.assertTrue(started.get());
          async.complete();
        } else {
          context.fail(ar.cause());
        }
      });
    });

    suite.run(options);
  }

  @Test
  public void testAsyncAssert4() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      AtomicBoolean started = new AtomicBoolean();
      Async async = context.async();
      vertx.deployVerticle(new AbstractVerticle() {
        @Override
        public void start(Future<Void> startFuture) throws Exception {
          started.set(true);
        }
      }, context.asyncAssertSuccess(id -> context.assertTrue(started.get())));

      suite.run(options);
    });
  }

  @Test
  public void testAsyncAssert5() {
//    Async async = context.async();
//    vertx.deployVerticle("my.verticle", ar1 -> {
//      if (ar1.succeeded()) {
//        vertx.deployVerticle("my.otherverticle", ar2 -> {
//          if (ar2.succeeded()) {
//            async.complete();
//          } else {
//            context.fail(ar2.cause());
//          }
//        });
//      } else {
//        context.fail(ar1.cause());
//      }
//    });
//
//// Can be replaced by
//
//    vertx.deployVerticle("my.verticle", context.asyncAssertSuccess(id ->
//                                                                           vertx.deployVerticle
// ("my_otherverticle", context.asyncAssertSuccess())
//    ));
  }

  @Test
  public void testAssert6() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      AtomicBoolean started = new AtomicBoolean();
      Async async = context.async();
      vertx.deployVerticle(new AbstractVerticle() {
        @Override
        public void start(Future<Void> startFuture) throws Exception {
          started.set(true);
        }
      }, ar -> {
        if (ar.succeeded()) {
          context.fail();
        } else {
          context.assertTrue(ar.cause() instanceof IllegalArgumentException);
          async.complete();
        }
      });
    });

    suite.run(options);
  }

  @Test
  public void testAssert7() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      AtomicBoolean started = new AtomicBoolean();
      Async async = context.async();
      vertx.deployVerticle(new AbstractVerticle() {
        @Override
        public void start(Future<Void> startFuture) throws Exception {
          started.set(true);
        }
      }, context.asyncAssertFailure(
              cause -> context.assertTrue(cause instanceof IllegalArgumentException)));
    });

    suite.run(options);
  }

  @Test
  public void testRepeat() {
//    When a test fails randomly or not often, for instance a race condition, it is convenient to
// run the same test multiple times to increase the failure likelihood of the test.
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    AtomicInteger count = new AtomicInteger(0);
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", 20, context -> {
      System.out.println(count.get());
      if (count.incrementAndGet() > 10) {
        context.fail();
      }
    });
    //When declared, beforeEach and afterEach callbacks will be executed as many times as the test is executed.

    suite.run(options);
  }

  @Test
  public void testShare() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> context.put("foo", "bar")).test("some_test1", context -> {
      context.assertEquals("bar", context.get("foo"));
    });

    suite.run(options);
  }

  @Test
  public void testSuiteComplete() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> context.put("foo", "bar")).test("some_test1", context -> {
      context.assertEquals("bar", context.get("foo"));
    });

    TestCompletion completion=  suite.run(options);

    // Simple completion callback
    completion.handler(ar -> {
      if (ar.succeeded()) {
        System.out.println("Test suite passed!");
      } else {
        System.out.println("Test suite failed:");
        ar.cause().printStackTrace();
      }
    });
  }

  @Test
  public void testInEventLoop() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("Async");
    suite.before(context -> vertx = Vertx.vertx()).test("some_test1", context -> {
      vertx.deployVerticle("com.edgar.vertx.unit.SomeVerticle", context.asyncAssertSuccess(id -> Context.isOnEventLoopThread()));
    });
    suite.run(options);
  }

  @Test
  public void testReport() {

    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"))
            .addReporter(new ReportOptions().
                    setTo("file:report.xml").
                    setFormat("junit"));
    TestSuite suite = TestSuite.create("Async");
    vertx = Vertx.vertx();
    suite.test("some_test1", context -> {
      vertx.deployVerticle("com.edgar.vertx.unit.SomeVerticle", context.asyncAssertSuccess(id -> Context.isOnEventLoopThread()));
    });
    suite.run(vertx, options);
  }
}
