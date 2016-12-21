package com.edgar.serviceproxy;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/11/29.
 *
 * @author Edgar  Date 2016/11/29
 */
public class Example2 {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    example2(vertx);
  }

  public static void example2(Vertx vertx) {
    // Assume database service is already deployed somewhere....

    // Create a proxy
    SomeDatabaseService service = SomeDatabaseService.createProxy(vertx,
                                                                  "database-service-address");

    // Save some data in the database - this time using the proxy
    service.save("mycollection", new JsonObject().put("name", "tim"), res2 -> {
      if (res2.succeeded()) {
        // done
      }
    });
  }
}
