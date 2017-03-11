package com.edgar.vertx.stream;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.Pump;

/**
 * Created by Administrator on 2015/9/1.
 */
public class PumpServer extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(PumpServer.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetServer().connectHandler(socket -> {
      socket.handler(buffer -> {
        Pump.pump(socket, socket).start();
      });
    }).listen(1234, ar -> {
      if (ar.succeeded()) {
        System.out.println("Echo server is now listening");
      } else {

      }
    });

  }
}