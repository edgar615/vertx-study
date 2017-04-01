package com.edgar.vertx.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class RedisZpopMain extends AbstractVerticle {

  private static final int count = 2;

  private Future<JsonArray> getArray(RedisClient redisClient, int length) {
    List<Future> futures = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      Future<String> future = Future.future();
      futures.add(future);
      redisClient.lpop("foo", ar -> {
        if (ar.succeeded()) {
          future.complete(ar.result());
        } else {
          future.fail(ar.cause());
        }
      });
    }
    Future<JsonArray> future = Future.future();
    CompositeFuture.all(futures)
            .setHandler(ar -> {
              JsonArray array = new JsonArray();
              CompositeFuture compositeFuture = ar.result();
              for (int i = 0; i < length; i++) {
                if (compositeFuture.succeeded(i)) {
                  if (compositeFuture.resultAt(i) != null) {
                    array.add((String) compositeFuture.resultAt(i));
                  }
                }
              }
              future.complete(array);
            });
    return future;
  }

  @Override
  public void start() throws Exception {
    RedisOptions config = new RedisOptions()
            .setHost("192.168.149.138")
            .setPort(6379);
    RedisClient redisClient = RedisClient.create(vertx, config);
    vertx.eventBus().<String>consumer("POP_FOO", received -> {
      Future<JsonArray> future = getArray(redisClient, count);
      future.setHandler(ar -> {
        if (ar.succeeded()) {
          System.out.println("hoho:" + ar.result());
        } else {
          ar.cause().printStackTrace();
        }
      });
    });

    for (int i = 0; i < 10; i++) {
      double d = Math.random();
      redisClient.lpush("foo", d + "", ar -> {
        if (ar.succeeded()) {
          vertx.eventBus().send("POP_FOO", "a");
        } else {
          ar.cause().printStackTrace();
        }
      });
    }

  }

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(RedisZpopMain.class.getName());
  }
}
