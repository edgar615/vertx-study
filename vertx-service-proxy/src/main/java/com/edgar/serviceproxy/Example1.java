package com.edgar.serviceproxy;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/11/29.
 *
 * @author Edgar  Date 2016/11/29
 */
public class Example1 {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    example1(vertx);
  }

  public static void example1(Vertx vertx) {
    // Assume database service is already deployed somewhere....
    // Save some data in the database
    JsonObject message = new JsonObject();
    message.put("collection", "mycollection")
            .put("document", new JsonObject().put("name", "tim"));
    DeliveryOptions options = new DeliveryOptions().addHeader("action", "save");
    vertx.eventBus().send("database-service-address", message, options, res2 -> {
      if (res2.succeeded()) {
        // done
      } else {
        // failure
      }
    });
  }
}
