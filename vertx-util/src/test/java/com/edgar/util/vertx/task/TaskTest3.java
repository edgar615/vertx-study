package com.edgar.util.vertx.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/4/29.
 *
 * @author Edgar  Date 2016/4/29
 */
public class TaskTest3 {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Task.<String>create((Future<String> f) -> {
      vertx.createHttpClient().post(8080, "10.4.7.220", "/login", response -> {
        response.bodyHandler(body -> {
          f.complete(body.toString());
        });
      }).setChunked(true)
              .write(new JsonObject()
                             .put("username", "12345678926")
                             .put("password", "123")
                             .put("channelId", "3434")
                             .put("deviceType", 3)
                             .encode())
              .end();
    }) .andThen("a", r -> System.out.println(r))
            .map("b", s -> s.substring(10, s.length() - 2))
            .andThen("token", t -> System.out.println(t))
            .map("l", t -> t.length())
            .andThen("i", i -> System.out.println(i))
            .flatMap("register", (Integer i, Future<String> f) -> {
              vertx.createHttpClient().get(8080, "10.4.7.220", "/registercode?tel=" + i, response
                      -> {
                response.bodyHandler(body -> {
                  f.complete(body.toString());
                });
              }).end();
            }).andThen("l", r -> System.out.println(r))
            .map("i", s -> s.length());

  }
}
