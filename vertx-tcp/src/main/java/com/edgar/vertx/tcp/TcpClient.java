package com.edgar.vertx.tcp;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class TcpClient extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(com.edgar.vertx.tcp.TcpClient.class);
  }

  @Override
  public void start() throws Exception {
//    A client can be configured to automatically retry connecting to the server in the event
// that it cannot connect. This is configured with setReconnectInterval and setReconnectAttempts.

//    Currently Vert.x will not attempt to reconnect if a connection fails, reconnect attempts
// and interval only apply to creating initial connections.
    NetClientOptions options = new NetClientOptions().setConnectTimeout(1000);
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
  }
}
