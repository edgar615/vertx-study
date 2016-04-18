package com.edgar.vertx.eventbus;

import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class PointToPointMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("com.edgar.vertx.eventbus.pointtopoint.Receiver");
    vertx.deployVerticle("com.edgar.vertx.eventbus.pointtopoint.Sender");
  }
}
