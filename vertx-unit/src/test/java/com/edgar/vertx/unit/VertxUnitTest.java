package com.edgar.vertx.unit;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;

public class VertxUnitTest {
  public static void main(String[] args) {
    new VertxUnitTest().run();
  }

  Vertx vertx;

  public void run() {
    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("com.edgar.vertx.unit.VertxUnitTest");
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
      HttpClient client = vertx.createHttpClient();
      Async async = context.async();
      client.getNow(8080, "localhost", "/", resp -> {
        resp.bodyHandler(body -> context.assertEquals("foo", body.toString("UTF-8")));
        client.close();
        async.complete();
      });
    });
    suite.test("some_test2", context -> {
// Deploy and undeploy a verticle
      vertx.deployVerticle("com.edgar.vertx.unit.SomeVerticle",
                           context.asyncAssertSuccess(deploymentID -> {
                             vertx.undeploy(deploymentID, context.asyncAssertSuccess());
                           }));
    });
    suite.run(options);
  }
}