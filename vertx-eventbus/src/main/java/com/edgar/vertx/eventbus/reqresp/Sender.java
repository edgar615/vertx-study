package com.edgar.vertx.eventbus.reqresp;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Sender extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Sender.class);
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();
    eb.consumer("news-feed", msg -> {
      msg.fail(456, "haha");
    });
    eb.send("news-feed", "some data", ar -> {
      if (ar.succeeded()) {
        System.out.println("ok");
      } else {
        System.out.println(ar.cause().getClass());
        ar.cause().printStackTrace();
      }
    });
  }
}
