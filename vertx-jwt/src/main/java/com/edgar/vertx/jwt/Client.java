package com.edgar.vertx.jwt;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * Created by Administrator on 2015/9/2.
 */
public class Client extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(1000, l -> {
      vertx.createHttpClient().post(8080, "localhost", "/login", resp -> {
        resp.bodyHandler(body -> {
          System.out.println(body.toString());
        });
      }).putHeader("content-type", "application/json").end(new JsonObject().put("username", "paulo")
                                                                                                 .put("password", "super_secret").encode());
    });
  }
}
