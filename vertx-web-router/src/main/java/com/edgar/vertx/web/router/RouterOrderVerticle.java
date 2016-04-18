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
public class RouterOrderVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(RouterOrderVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.route("/foo").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();

      response.write("route1\n");

      routingContext.vertx().setTimer(1000, l -> routingContext.next());
    });

    router.route("/foo").order(-1).handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.setChunked(true);
      response.write("route2\n");

      routingContext.vertx().setTimer(1000, l -> routingContext.next());
    });

    router.route("/foo").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();

      response.write("route3\n");

      response.end();
    });

    server.requestHandler(router::accept).listen(8080);
  }
}
