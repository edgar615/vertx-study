package com.edgar.vertx.http.pump;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    //The server response is a WriteStream instance so you can pump to it from any ReadStream, e
    // .g. AsyncFile, NetSocket, WebSocket or HttpServerRequest.
    vertx.createHttpClient().put(8080, "localhost", "/", response -> {
      response.bodyHandler(body -> System.out.println(body.toJsonObject().encodePrettily()));
    }).end(new JsonObject().put("foo", "bar").encode());
  }
}
