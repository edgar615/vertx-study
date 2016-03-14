package com.edgar.vertx.tcp;

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
public class SharedTcpServer2 extends AbstractVerticle {

  public static void main(String[] args) {


//    When you deploy another server on the same host and port as an existing server it doesn’t
// actually try and create a new server listening on the same host/port.
//            Instead it internally maintains just a single server, and, as incoming connections
// arrive it distributes them in a round-robin fashion to any of the connect handlers.
//            Consequently Vert.x TCP servers can scale over available cores while each instance
// remains single threaded.

    //TODO 实验不成功，抛出java.net.BindException: Address already in use: bind
    DeploymentOptions options = new DeploymentOptions().setInstances(10);
    Vertx.vertx().deployVerticle("com.edgar.vertx.tcp.SharedTcpServer2", options);
    Runner.runExample(SharedTcpServer2.class);
  }

  @Override
  public void start() throws Exception {
    NetServerOptions options = new NetServerOptions().setPort(1234);
    NetServer server = vertx.createNetServer(options);
    server.connectHandler(socket -> {
      socket.handler(buffer -> {
        // Just echo back the data
        socket.write(buffer);
      });
    });
    server.listen();
  }
}
