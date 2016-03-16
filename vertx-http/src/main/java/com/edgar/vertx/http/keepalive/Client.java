package com.edgar.vertx.http.keepalive;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {


//    When keep alive is enabled. Vert.x will add a Connection: Keep-Alive header to each HTTP/1
// .0 request sent. When keep alive is disabled. Vert.x will add a Connection: Close header to
// each HTTP/1.1 request sent to signal that the connection will be closed after completion of
// the response.
//
//            The maximum number of connections to pool for each server is configured using
// setMaxPoolSize
//
//    When making a request with pooling enabled, Vert.x will create a new connection if there
// are less than the maximum number of connections already created for that server, otherwise it
// will add the request to a queue.
//
//    Keep alive connections will not be closed by the client automatically. To close them you
// can close the client instance.
//
//            Alternatively you can set idle timeout using setIdleTimeout - any connections not
// used within this timeout will be closed. Please note the idle timeout value is in seconds not
// milliseconds.

            HttpClientOptions options = new HttpClientOptions().setKeepAlive(true).setMaxPoolSize(10);
    vertx.createHttpClient().get(8080, "localhost", "/", response -> {
      System.out.print(response.headers().get("Content-Encoding"));
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toJsonObject().encodePrettily()));
    }).end();

  }
}
