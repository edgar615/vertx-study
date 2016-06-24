package com.edgar.vertx.redis.lua;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class RedisLuaMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    RedisOptions config = new RedisOptions()
            .setHost("10.4.7.220");
    RedisClient redisClient = RedisClient.create(vertx, config);
    vertx.fileSystem().readFile("limit.lua", result -> {
      if (result.succeeded()) {
        redisClient.eval(result.result().toString(), Arrays.asList("ip:"+System.currentTimeMillis
                () / 60000)
                , Arrays.asList("3"), ar -> {
          if (ar.succeeded()) {
            System.out.println(ar.result().encode());
            //如果结果是1，表示限流通过，不为1，表示阻塞
          } else {
            System.err.println("Oh oh ..." + ar.cause());
          }
        });

        redisClient.eval(result.result().toString(), Arrays.asList("ip:"+System.currentTimeMillis
                () / 60000)
                , Arrays.asList("3"), ar -> {
          if (ar.succeeded()) {
            System.out.println(ar.result().encode());
            //如果结果是1，表示限流通过，不为1，表示阻塞
          } else {
            System.err.println("Oh oh ..." + ar.cause());
          }
        });

        redisClient.eval(result.result().toString(), Arrays.asList("ip:"+System.currentTimeMillis
                () / 60000)
                , Arrays.asList("3"), ar -> {
          if (ar.succeeded()) {
            System.out.println(ar.result().encode());
            //如果结果是1，表示限流通过，不为1，表示阻塞
          } else {
            System.err.println("Oh oh ..." + ar.cause());
          }
        });

        redisClient.eval(result.result().toString(), Arrays.asList("ip:"+System.currentTimeMillis
                () / 60000)
                , Arrays.asList("3"), ar -> {
          if (ar.succeeded()) {
            System.out.println(ar.result().encode());
            //如果结果是1，表示限流通过，不为1，表示阻塞
          } else {
            System.err.println("Oh oh ..." + ar.cause());
          }
        });

        redisClient.eval(result.result().toString(), Arrays.asList("ip:"+System.currentTimeMillis
                () / 60000)
                , Arrays.asList("3"), ar -> {
          if (ar.succeeded()) {
            System.out.println(ar.result().encode());
            //如果结果是1，表示限流通过，不为1，表示阻塞
          } else {
            System.err.println("Oh oh ..." + ar.cause());
          }
        });
      } else {
        System.err.println("Oh oh ..." + result.cause());
      }
    });
  }
}
