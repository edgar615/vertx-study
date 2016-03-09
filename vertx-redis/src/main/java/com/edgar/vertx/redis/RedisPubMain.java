package com.edgar.vertx.redis;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class RedisPubMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
//    RedisOptions redisOptions = new RedisOptions();
//    redisOptions.setAddress("192.168.149.138");
    RedisOptions config = new RedisOptions()
            .setHost("192.168.149.138");
    RedisClient redisClient = RedisClient.create(vertx, config);
    redisClient.publish("channel1", "Hello World!", res -> {
      if (res.succeeded()) {
        System.out.println("pub" + res.result());
      } else {
        System.err.println("pub error" + res.cause().getMessage());
      }
    });

  }
}
