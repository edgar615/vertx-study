package com.edgar.vertx.tcp;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class SharedTcpServer extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(SharedTcpServer.class);
  }

  @Override
  public void start() throws Exception {


//    The handlers of any TCP server are always executed on the same event loop thread.
//            This means that if you are running on a server with a lot of cores, and you only have this one instance deployed then you will have at most one core utilised on your server.
//            In order to utilise more cores of your server you will need to deploy more instances of the server.

//    When you deploy another server on the same host and port as an existing server it doesn’t
// actually try and create a new server listening on the same host/port.
//            Instead it internally maintains just a single server, and, as incoming connections
// arrive it distributes them in a round-robin fashion to any of the connect handlers.
//            Consequently Vert.x TCP servers can scale over available cores while each instance
// remains single threaded.

    NetServerOptions options = new NetServerOptions().setPort(1234);
    for (int i = 0; i < 10; i++) {
      NetServer server = vertx.createNetServer(options);
      final String serverNo = "server." + i;
      server.connectHandler(socket -> {
        socket.handler(buffer -> {
          // Just echo back the data
          buffer.appendString(serverNo);
          socket.write(buffer);
        });
      });
      server.listen(1234, "localhost");
    }
  }
}
