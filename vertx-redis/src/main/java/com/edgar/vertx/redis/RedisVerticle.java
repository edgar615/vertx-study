package com.edgar.vertx.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Created by Edgar on 2016/3/9.
 *
 * @author Edgar  Date 2016/3/9
 */
public class RedisVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    //    host: default is localhost
//    port: default is 6379
//    encoding: default is UTF-8
//    tcpKeepAlive: default true
//    tcpNoDelay: default true
    RedisOptions config = new RedisOptions()
            .setHost("192.168.149.138");
    RedisClient redisClient = RedisClient.create(vertx, config);
    redisClient.set("foo", "bar", res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        System.err.println("error");
      }
    }).get("foo", res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        System.err.println("error");
      }
    }).hset("my-hash", "foo", "bar", res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        System.err.println("error:" + res.cause().getMessage());
      }
    }).hset("my-hash", "bar", "foo", res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        System.err.println("error:" + res.cause().getMessage());
      }
    }).hgetall("my-hash", res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        System.err.println("error:" + res.cause().getMessage());
      }
    }).info(res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        System.err.println("error:" + res.cause().getMessage());
      }
    });
  }
}
