package com.edgar.vertx.healthcheck;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

/**
 * Created by edgar on 17-3-9.
 */
public class GroupVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(GroupVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

    Router router = Router.router(vertx);
    router.get("/health").handler(healthCheckHandler);
    router.get("/health/*").handler(healthCheckHandler);

    healthCheckHandler.register("a-group/my-procedure-name", future -> {
      future.complete(Status.OK());
//      future.fail("undefined error");
    });

    healthCheckHandler.register("a-group/a-second-group/my-second-procedure-name", future -> {
      future.complete(Status.KO());
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}
