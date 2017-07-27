package com.edgar.vertx.sharedata.lock;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class LockVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(LockVerticle.class);
  }

  @Override
  public void start() throws Exception {
    SharedData sharedData = vertx.sharedData();
    sharedData.getLock("mylock", res -> {
      if (res.succeeded()) {
        // Got the lock!
        Lock lock = res.result();
        System.out.println("op1:" + System.currentTimeMillis());
        // 5 seconds later we release the lock so someone else can get it

        vertx.setTimer(5000, tid -> lock.release());

      } else {
        // Something went wrong
      }
    });

    sharedData.getLock("mylock", res -> {
      if (res.succeeded()) {
        // Got the lock!
        Lock lock = res.result();
        System.out.println("op2:" + System.currentTimeMillis());
        // 5 seconds later we release the lock so someone else can get it

        vertx.setTimer(5000, tid -> lock.release());

      } else {
        // Something went wrong
      }
    });

    sharedData.getLockWithTimeout("mylock", 1000, res -> {
      if (res.succeeded()) {
        // Got the lock!
        Lock lock = res.result();
        System.out.println("op3:" + System.currentTimeMillis());
        // 5 seconds later we release the lock so someone else can get it

        vertx.setTimer(5000, tid -> lock.release());

      } else {
        // Something went wrong
        System.out.println("error");
      }
    });
  }
}
