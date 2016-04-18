package com.edgar.vertx.web.router;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class ErrorHandlerVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(ErrorHandlerVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //Vert.x Web parses the Accept-Language header and provides some helper methods to identify
    // which is the preferred locale for a client or the sorted list of preferred locales by
    // quality.
    Router router = Router.router(vertx);
    Route route1 = router.get("/somepath/path1/");

    route1.handler(routingContext -> {

      // Let's say this throws a RuntimeException
      throw new RuntimeException("something happened!");

    });

    Route route2 = router.get("/somepath/path2");

    route2.handler(routingContext -> {

      // This one deliberately fails the request passing in the status code
      // E.g. 403 - Forbidden
      routingContext.put("foo", "bar");
      routingContext.fail(403);

    });

// Define a failure handler
// This will get called for any failures in the above handlers
    Route route3 = router.get("/somepath/*");

    route3.failureHandler(failureRoutingContext -> {

      int statusCode = failureRoutingContext.statusCode();
      String bar = failureRoutingContext.get("foo");

      // Status code will be 500 for the RuntimeException or 403 for the other failure
      HttpServerResponse response = failureRoutingContext.response();
      response.setStatusCode(statusCode).end("Sorry! Not today" + bar);

    });
    server.requestHandler(router::accept).listen(8080);
  }
}
