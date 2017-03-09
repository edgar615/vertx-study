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
public class StatusVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(StatusVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

    Router router = Router.router(vertx);
    router.get("/health").handler(healthCheckHandler);

    healthCheckHandler.register("my-procedure-name", future -> {
      future.complete(Status.OK(new JsonObject().put("available-memory", "2mb")));
    });

    healthCheckHandler.register("my-second-procedure-name", future -> {
      future.complete(Status.KO(new JsonObject().put("load", 99)));
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}
