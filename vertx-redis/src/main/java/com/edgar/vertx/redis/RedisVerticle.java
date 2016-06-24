package com.edgar.vertx.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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
            .setHost("192.168.149.138")
            .setPort(6379);
//            .setAuth("Edgar");
    RedisClient redisClient = RedisClient.create(vertx, config);
//    Future<String> multiFuture = Future.future();
//    redisClient.multi(multiFuture.completer());
//    multiFuture.setHandler(ar -> {
//      System.out.println(ar.result());
//    });
//    redisClient.multi(ar -> {
//      System.out.println(ar.result());
//    }).spop("foo", ar -> {
//      System.out.println(ar.result());
//    }).spop("foo", ar -> {
//      System.out.println(ar.result());
//    }).sadd("foo", "3", ar -> {
//      System.out.println(ar.result());
//    }).sadd("foo", "4", ar -> {
//      System.out.println(ar.result());
//    }).exec(ar -> {
//      System.out.println(ar.result());
//    });

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
