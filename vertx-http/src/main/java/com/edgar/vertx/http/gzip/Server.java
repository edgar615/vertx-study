package com.edgar.vertx.http.gzip;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {


//    By default compression is not enabled.
//
//    When HTTP compression is enabled the server will check if the client includes an
// Accept-Encoding header which includes the supported compressions. Commonly used are deflate
// and gzip. Both are supported by Vert.x.
//
//            If such a header is found the server will automatically compress the body of the
// response with one of the supported compressions and send it back to the client.
//
//    Be aware that compression may be able to reduce network traffic but is more CPU-intensive.

            HttpServerOptions options = new HttpServerOptions().setCompressionSupported(true);
    vertx.createHttpServer(options).requestHandler(request -> {
      JsonObject jsonObject = new JsonObject();
      jsonObject.put("method", request.method());
      jsonObject.put("version", request.version());
      jsonObject.put("uri", request.uri());
      jsonObject.put("path", request.path());
      jsonObject.put("query", request.query());
      jsonObject.put("remoteAddress", request.remoteAddress().toString());
      jsonObject.put("absoluteURI", request.absoluteURI());
      MultiMap headers = request.headers();
      jsonObject.put("user-agent", headers.get("user-agent"));
      jsonObject.put("Accept-Encoding", "Accept-Encoding");
      jsonObject.put("headers", headers.toString());
      jsonObject.put("params", request.params().toString());

      request.response().setChunked(true).end(jsonObject.encode());

    }).listen(8080, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server is now listening!");
      } else {
        System.out.println("Failed to bind!");
      }
    });
  }
}
