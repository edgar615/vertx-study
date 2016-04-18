package com.edgar.vertx.web.router;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ErrorHandler;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class ErrorHandlerExample extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(ErrorHandlerExample.class);
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

    }).failureHandler(ErrorHandler.create());

    server.requestHandler(router::accept).listen(8080);
  }
}
