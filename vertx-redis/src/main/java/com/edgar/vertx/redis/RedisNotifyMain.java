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
public class RedisNotifyMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis.__keyevent@0__:expired", received -> {
      JsonObject value = received.body().getJsonObject("value");
      System.out.println(value);
    });
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis.__keyevent@0__:del", received -> {
      JsonObject value = received.body().getJsonObject("value");
      System.out.println(value);
    });
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis.__keyspace@0__:foo", received -> {
      JsonObject value = received.body().getJsonObject("value");
      System.out.println(value);
    });
    RedisOptions config = new RedisOptions()
            .setHost("192.168.149.138");
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
    redisClient.subscribeMany(Arrays.asList("__keyevent@0__:expired", "__keyevent@0__:del"), res
            -> {
      if (res.succeeded()) {
        System.out.println("sub" + res.result());
      } else {
        System.err.println("sub error" + res.cause().getMessage());
      }
    });
    redisClient.subscribe("__keyspace@0__:foo", res -> {
      if (res.succeeded()) {
        System.out.println("sub" + res.result());
      } else {
        System.err.println("sub error" + res.cause().getMessage());
      }
    });

  }
}
