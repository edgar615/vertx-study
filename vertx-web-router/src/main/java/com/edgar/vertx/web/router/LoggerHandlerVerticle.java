package com.edgar.vertx.web.router;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class LoggerHandlerVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(LoggerHandlerVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //Vert.x Web parses the Accept-Language header and provides some helper methods to identify
    // which is the preferred locale for a client or the sorted list of preferred locales by
    // quality.
    Router router = Router.router(vertx);
    router.get("/foo/*").handler(rc -> {
      HttpServerResponse response = rc.response();
      response.putHeader("content-type", "text/plain");

      response.end(rc.normalisedPath());
    });
    router.route().handler(LoggerHandler.create());

    server.requestHandler(router::accept).listen(8080);
  }
}
