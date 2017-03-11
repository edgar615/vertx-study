package com.edgar.vertx.stream;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/9/1.
 */
public class CheckFullServer extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(CheckFullServer.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetServer().connectHandler(socket -> {
      socket.handler(buffer -> {
        if (!socket.writeQueueFull()) {
          socket.write(buffer);
        }
      });
    }).listen(1234, ar -> {
      if (ar.succeeded()) {
        System.out.println("Echo server is now listening");
      } else {

      }
    });

  }
}