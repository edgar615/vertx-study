package com.edgar.vertx.jwt;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.util.Base64;

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
            try {
                String token = body.toJsonObject().getString("token");
                String[] tokens = token.split("\\.");
                String claim = tokens[1];
                String claimJson = new String(Base64.getDecoder().decode(claim));
                System.out.println(claimJson);

                token = body.toJsonObject().getString("expToken");
                 tokens = token.split("\\.");
                claim = tokens[1];
                claimJson = new String(Base64.getDecoder().decode(claim));
                System.out.println(claimJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
      }).putHeader("content-type", "application/json").end(new JsonObject().put("username", "paulo")
                                                                                                 .put("password", "super_secret").encode());
    });
  }
}
