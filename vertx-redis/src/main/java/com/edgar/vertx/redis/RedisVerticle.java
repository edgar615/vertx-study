package com.edgar.vertx.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
            .setHost("10.11.0.31")
            .setPort(6379);
//            .setAuth("Edgar");
    RedisClient redisClient = RedisClient.create(vertx, config);

    redisClient.set("string_key", new JsonObject().put("a", 1)
            .put("b", new JsonObject().put("foo", "bar")
                    .put("c", new JsonArray().add(0))).encode(), ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
      } else {
        System.err.println("error");
      }
    }).get("string_key", ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
        JsonObject jsonObject = new JsonObject(ar.result());
      } else {
        System.err.println("error");
      }
    });

//    redisClient.hmset("hash_key", new JsonObject().put("a", 1)
//            .put("b", new JsonObject().put("foo", "bar")
//            .put("c", new JsonArray().add(0))), ar -> {
//      if (ar.succeeded()) {
//        System.out.println(ar.result());
//      } else {
//        System.err.println("error");
//      }
//    }).hgetall("hash_key", ar -> {
//      if (ar.succeeded()) {
//        System.out.println(ar.result());
//        JsonObject b = ar.result().getJsonObject("b");//error
//        System.out.println(b);
//      } else {
//        System.err.println("error");
//      }
//    });
//    redisClient.setnx("foo", "bar", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error");
//      }
//    }).setnx("foo", "bar", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error");
//      }
//    }).setnx("foo", "2", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error");
//      }
//    });
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

//    redisClient.set("foo", "bar", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error");
//      }
//    }).get("foo", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error");
//      }
//    }).hset("my-hash", "foo", "bar", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error:" + res.cause().getMessage());
//      }
//    }).hset("my-hash", "bar", "foo", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error:" + res.cause().getMessage());
//      }
//    }).hgetall("my-hash", res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error:" + res.cause().getMessage());
//      }
//    }).info(res -> {
//      if (res.succeeded()) {
//        System.out.println(res.result());
//      } else {
//        System.err.println("error:" + res.cause().getMessage());
//      }
//    });
  }
}
