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
public class EventbusExposeVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(EventbusExposeVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    HealthChecks hc = HealthChecks.create(vertx);

    hc.register("my-procedure", future -> future.complete(Status.OK()));

    vertx.eventBus().consumer("health",
        message -> hc.invoke(message::reply));

    vertx.setPeriodic(2000l, l -> {
      vertx.eventBus().send("health", "", ar ->{
        System.out.println(ar.result().body());
      });
    });

  }
}
