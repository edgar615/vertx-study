package com.edgar.vertx.tcp;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServerOptions;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class TcpServer extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(com.edgar.vertx.tcp.TcpServer.class);
  }

  @Override
  public void start() throws Exception {
    NetServerOptions options = new NetServerOptions().setPort(1234);
    vertx.createNetServer(options).connectHandler(socket -> {
      //Closed handler
      socket.closeHandler(v -> System.out.println("The socket has been closed"));
      //Handling exceptions
      socket.exceptionHandler(t -> System.err.println(t.getMessage()));

//Reading data from the socket
      socket.handler(buffer -> {
        System.out.println("I received some bytes: " + buffer);
      });
      //Writing data to a socket
      socket.write("hello");
      Buffer buffer = Buffer.buffer().appendFloat(12.34f).appendInt(123);
      socket.write(buffer);
//// Write a string in UTF-8 encoding
      socket.write("some data");
//// Write a string using the specified encoding
//      socket.write("some data", "UTF-16");
      //Sending files or resources from the classpath
      socket.sendFile("myfile.dat");
      //Streaming sockets
    }).listen(res -> {
      if (res.succeeded()) {
        System.out.println("Server is now listening!");
      } else {
        System.out.println("Failed to bind!" + res.cause());
      }
    });

    //close server
//    server.close(res -> {
//      if (res.succeeded()) {
//        System.out.println("Server is now closed");
//      } else {
//        System.out.println("close failed");
//      }
//    });

    //Listening on a random port
//    NetServer server = vertx.createNetServer();
//    server.listen(0, "localhost", res -> {
//      if (res.succeeded()) {
//        System.out.println("Server is now listening on actual port: " + server.actualPort());
//      } else {
//        System.out.println("Failed to bind!");
//      }
//    });
  }
}
