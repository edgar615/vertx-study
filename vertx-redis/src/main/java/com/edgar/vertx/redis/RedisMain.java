package com.edgar.vertx.redis;

import com.edgar.util.vertx.runner.Runner;

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
