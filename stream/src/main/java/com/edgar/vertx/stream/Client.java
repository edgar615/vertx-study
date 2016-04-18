package com.edgar.vertx.stream;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetSocket;

/**
 * Created by Administrator on 2015/9/1.
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetClient().connect(1234, "localhost", res -> {
      if (res.succeeded()) {
        NetSocket socket = res.result();
        socket.handler(buffer -> {
          System.out.println("Net client receiving: " + buffer.toString("UTF-8"));
        });
        // Now send some data
        for (int i = 0; i < 10; i++) {
          String str = "hello " + i + "\n";
          System.out.println("Net client sending: " + str);
          socket.write(str);
        }
      } else {
        System.out.println("Failed to connect " + res.cause());
      }
    });
  }
}