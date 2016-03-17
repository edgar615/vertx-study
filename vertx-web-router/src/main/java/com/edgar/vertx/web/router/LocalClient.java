package com.edgar.vertx.web.router;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class LocalClient extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(LocalClient.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpClient().get(8080, "localhost", "/localized", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Accept-Language", "fr").end();

    vertx.createHttpClient().get(8080, "localhost", "/localized", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Accept-Language", "pt").end();


    vertx.createHttpClient().get(8080, "localhost", "/localized", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Accept-Language", "zh").end();
  }
}
