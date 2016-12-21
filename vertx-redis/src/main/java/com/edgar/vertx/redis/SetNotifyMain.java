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
public class SetNotifyMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis.__keyevent@0__:set", received -> {
      JsonObject value = received.body().getJsonObject("value");
      System.out.println(value);
    });
    RedisOptions config = new RedisOptions()
            .setHost("10.11.0.31");
    RedisClient redisClient = RedisClient.create(vertx, config);
//    redisClient.subscribe("__keyevent@0__:expired", res -> {
//      if (res.succeeded()) {
//        System.out.println("sub" + res.result());
//      } else {
//        System.err.println("sub error" + res.cause().getMessage());
//      }
//    });
//    redisClient.subscribe("__keyevent@0__:set", res -> {
//      if (res.succeeded()) {
//        System.out.println("sub" + res.result());
//      } else {
//        System.err.println("sub error" + res.cause().getMessage());
//      }
//    });
    redisClient.subscribeMany(Arrays.asList("__keyevent@0__:lpush","__keyevent@0__:set",
                                            "__keyevent@0__:del"), res
            -> {
      if (res.succeeded()) {
        System.out.println("sub" + res.result());
      } else {
        System.err.println("sub error" + res.cause().getMessage());
      }
    });

  }
}
