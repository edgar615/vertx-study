package com.edgar.vertx.sharedata.lock;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;

import java.util.UUID;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class LockMemVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(LockMemVerticle.class);
  }

  @Override
  public void start() throws Exception {
    SharedData sharedData = vertx.sharedData();
    for (int i = 0; i < 10000000; i ++) {
      sharedData.getLock("mylock" + UUID.randomUUID().toString(), res -> {
        if (res.succeeded()) {
          // Got the lock!
          Lock lock = res.result();
//          System.out.println("op1:" + System.currentTimeMillis());
          // 5 seconds later we release the lock so someone else can get it

          vertx.setTimer(1000, tid -> lock.release());

        } else {
          // Something went wrong
        }
      });
    }
  }
}
