package com.edgar.vertx.web.router;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class BodyHandlerLimitVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(BodyHandlerLimitVerticle.class);
  }

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    //    If an attempt to send a body greater than the maximum size is made, an HTTP status code of
// 413 - Request Entity Too Large, will be sent.
//    There is no body limit by default.
    Route route = router.route().handler(BodyHandler.create().setBodyLimit(10));
    router.post("/some/path").handler(routingContext -> {
      //Getting the request body
      routingContext.response().setChunked(true).write("getBodyAsString:" + routingContext
              .getBodyAsString())
      .write("getBodyAsJson:" + routingContext.getBodyAsJson()).end();

    });

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
