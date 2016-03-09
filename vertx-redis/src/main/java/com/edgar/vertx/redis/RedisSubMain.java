package com.edgar.vertx.redis;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class RedisSubMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis.channel1", received -> {
      // do whatever you need to do with your message
      JsonObject value = received.body().getJsonObject("value");
      System.out.println(value);
      // the value is a JSON doc with the following properties
      // channel - The channel to which this message was sent
      // pattern - Pattern is present if you use psubscribe command and is the pattern that matched this message channel
      // message - The message payload
    });

    RedisOptions config = new RedisOptions()
            .setHost("192.168.149.138");
    RedisClient redisClient = RedisClient.create(vertx, config);
    redisClient.subscribe("channel1", res -> {
      if (res.succeeded()) {
        System.out.println("pub" + res.result());
      } else {
        System.err.println("pub error" + res.cause().getMessage());
      }
    });

  }
}
