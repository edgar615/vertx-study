package com.edgar.vertx.redis;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.Arrays;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class IncrbyNotifyMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis.__keyevent@0__:incrby", received -> {
      JsonObject value = received.body().getJsonObject("value");
      System.out.println(value);
    });
    RedisOptions config = new RedisOptions()
            .setHost("10.11.0.31");
    RedisClient redisClient = RedisClient.create(vertx, config);
    redisClient.subscribeMany(Arrays.asList(
                                            "__keyevent@0__:incrby"), res
            -> {
      if (res.succeeded()) {
        System.out.println("sub" + res.result());
      } else {
        System.err.println("sub error" + res.cause().getMessage());
      }
    });

  }
}
