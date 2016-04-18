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
public class SubRouterVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(SubRouterVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //SubRoute
    Router router = Router.router(vertx);
    router.route("/foo").handler(rc -> {
      HttpServerResponse response = rc.response();
      response.putHeader("content-type", "text/plain");

      response.end(rc.normalisedPath());
    });

    Router restAPI = Router.router(vertx);

    restAPI.get("/products/:productID").handler(rc -> {
      rc.response().end("produceID:" + rc.request().getParam("productID"));
    });

    //http://localhost:8080/productsAPI/products/123
    router.mountSubRouter("/productsAPI", restAPI);

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
