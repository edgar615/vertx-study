package com.edgar.vertx.web.router;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class BodyHandlerClient extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(BodyHandlerClient.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpClient().post(8080, "localhost", "/some/path", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).setChunked(true).write(new JsonObject().put("foo", "bar").encode()).end();

  }
}
