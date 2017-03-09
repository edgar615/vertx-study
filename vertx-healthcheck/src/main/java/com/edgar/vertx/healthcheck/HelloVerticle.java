package com.edgar.vertx.healthcheck;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

/**
 * Created by edgar on 17-3-9.
 */
public class HelloVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(HelloVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    HealthChecks hc = HealthChecks.create(vertx);

    hc.register("my-procedure", future -> future.complete(Status.OK()));

    HealthCheckHandler healthCheckHandler = HealthCheckHandler.createWithHealthChecks(hc);

    Router router = Router.router(vertx);
    router.get("/ping").handler(healthCheckHandler);

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}
