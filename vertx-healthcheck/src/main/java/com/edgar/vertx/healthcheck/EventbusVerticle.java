package com.edgar.vertx.healthcheck;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;

/**
 * Created by edgar on 17-3-9.
 */
public class EventbusVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(EventbusVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

    Router router = Router.router(vertx);
    router.get("/health").handler(healthCheckHandler);

    vertx.eventBus().consumer("health", msg -> {
      msg.reply("pong");
    });

    healthCheckHandler.register("receiver",
        future ->
            vertx.eventBus().send("health", "ping", response -> {
              if (response.succeeded()) {
                future.complete(Status.OK());
              } else {
                future.complete(Status.KO());
              }
            })
    );
    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}
