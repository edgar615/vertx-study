package com.edgar.vertx.tcp.share;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class TcpServerVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    NetServer server = vertx.createNetServer();
    server.connectHandler(socket -> {
      socket.handler(buffer -> {
        // Just echo back the data
        socket.write("Hello from " + this);
      });
    });
    server.listen(1234);
  }
}