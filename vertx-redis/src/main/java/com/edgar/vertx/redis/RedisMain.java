package com.edgar.vertx.redis;

import com.edgar.vertx.util.Runner;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;

/**
 * Created by Edgar on 2016/3/8.
 *
 * @author Edgar  Date 2016/3/8
 */
public class RedisMain {
  public static void main(String[] args) {
    Runner.runExample(com.edgar.vertx.redis.RedisVerticle.class);
  }
}
