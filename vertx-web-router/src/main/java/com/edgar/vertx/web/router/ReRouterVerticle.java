package com.edgar.vertx.web.router;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class ReRouterVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(ReRouterVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //Reroute
    Router router = Router.router(vertx);
    router.get("/some/path").handler(routingContext -> {

      routingContext.put("foo", "bar");
      routingContext.next();

    });

    router.get("/some/path/B").handler(routingContext -> {
      routingContext.response().end("Reroute");
    });

    router.get("/some/path").handler(routingContext -> {
      routingContext.reroute("/some/path/B");
    });

    router.route().handler(routingContext -> {

      // This handler will be called for every request
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "text/plain");

      // Write to the response and end it
      response.end("Hello World from Vert.x-Web!");
    });

    server.requestHandler(router::accept).listen(8080);
  }
}
