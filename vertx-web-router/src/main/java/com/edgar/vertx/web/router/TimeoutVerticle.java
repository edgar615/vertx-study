package com.edgar.vertx.web.router;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class TimeoutVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(TimeoutVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //Vert.x Web parses the Accept-Language header and provides some helper methods to identify
    // which is the preferred locale for a client or the sorted list of preferred locales by
    // quality.
    Router router = Router.router(vertx);
    router.get("/foo/*").handler(TimeoutHandler.create(5)).handler(rc -> {
      HttpServerResponse response = rc.response();
      response.putHeader("content-type", "text/plain");

      response.end(rc.normalisedPath());
    });

    server.requestHandler(router::accept).listen(8080);
  }
}
