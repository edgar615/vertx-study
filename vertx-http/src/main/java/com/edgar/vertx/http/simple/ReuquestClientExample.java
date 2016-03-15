package com.edgar.vertx.http.simple;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class ReuquestClientExample extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(com.edgar.vertx.http.simple.ReuquestClientExample.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpClient().get(8080, "localhost", "/", response -> {
      System.out.print(response.trailers().names());
      System.out.print(response.headers().names());
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toJsonObject().encodePrettily()));
    }).end();
  }
}
