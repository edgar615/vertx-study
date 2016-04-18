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
public class NextHandlerVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(NextHandlerVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.route("/foo").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      // enable chunked responses because we will be adding data as
      // we execute over other handlers. This is only required once and
      // only if several handlers do output.
      response.setChunked(true);

      response.write("route1\n");

      // Call the next matching route after a 5 second delay
      routingContext.vertx().setTimer(5000, l -> routingContext.next());
    });

    router.route("/foo").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();

      response.write("route2\n");

      // Call the next matching route after a 5 second delay
      routingContext.vertx().setTimer(5000, l -> routingContext.next());
    });

    router.route("/foo").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();

      response.write("route3\n");

      response.end();
    });

    server.requestHandler(router::accept).listen(8080);
  }
}
