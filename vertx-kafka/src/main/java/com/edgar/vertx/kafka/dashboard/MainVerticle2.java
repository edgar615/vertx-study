package com.edgar.vertx.kafka.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainVerticle2 extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle2());
  }

  @Override
  public void start() throws Exception {

    vertx.deployVerticle(
      DashboardVerticle.class.getName()
    );

    // Deploy the metrics collector : 3 times
    vertx.deployVerticle(
      MetricsVerticle.class.getName()
    );
  }

  @Override
  public void stop() throws Exception {
//    kafkaCluster.shutdown();
  }
}