package com.edgar.vertx.tcp.share;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class Client extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    NetClientOptions options = new NetClientOptions().setConnectTimeout(1000);
    vertx.setPeriodic(1000, l -> {
      vertx.createNetClient(options).connect(1234, "localhost", res -> {
        if (res.succeeded()) {
          System.out.println("Connected!");
          NetSocket socket = res.result();
          socket.handler(buffer -> {
            System.out.println("Net client receiving: " + buffer.toString("UTF-8"));
          });
          for (int i = 0; i < 10; i++) {
            String str = "hello " + i + "\n";
            socket.write(str);
          }
        } else {
          System.out.println("Failed to connect: " + res.cause().getMessage());
        }
      });
    });
  }
}
