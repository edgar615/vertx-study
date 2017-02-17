package com.edgar.vertx.redis.lua;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.Arrays;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class LimitMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    RedisOptions config = new RedisOptions()
            .setHost("10.11.0.31");
    RedisClient redisClient = RedisClient.create(vertx, config);
    vertx.fileSystem().readFile("limit.lua", result -> {
      if (result.succeeded()) {
        System.out.println(result.result().toString());
        redisClient.eval(result.result().toString(), Arrays.asList("ip:192.168.1.110")
                , Arrays.asList("2", "3"), ar -> {
                  if (ar.succeeded()) {
                    System.out.println(ar.result().encode());
                    //如果结果是1，表示限流通过，不为1，表示阻塞
                  } else {
                  }
                });
        redisClient.eval(result.result().toString(), Arrays.asList("ip:192.168.1.110")
                , Arrays.asList("2", "3"), ar -> {
                  if (ar.succeeded()) {
                    System.out.println(ar.result().encode());
                    //如果结果是1，表示限流通过，不为1，表示阻塞
                  } else {
                  }
                });
        redisClient.eval(result.result().toString(), Arrays.asList("ip:192.168.1.110")
                , Arrays.asList("2", "3"), ar -> {
                  if (ar.succeeded()) {
                    System.out.println(ar.result().encode());
                    //如果结果是1，表示限流通过，不为1，表示阻塞
                  } else {
                  }
                });
        redisClient.eval(result.result().toString(), Arrays.asList("ip:192.168.1.110")
                , Arrays.asList("2", "3"), ar -> {
                  if (ar.succeeded()) {
                    System.out.println(ar.result().encode());
                    //如果结果是1，表示限流通过，不为1，表示阻塞
                  } else {
                  }
                });
      } else {
      }
    });
  }
}
