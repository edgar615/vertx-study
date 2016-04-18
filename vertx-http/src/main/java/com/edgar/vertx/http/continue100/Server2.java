package com.edgar.vertx.http.continue100;

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
public class Server2 extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server2.class);
  }

  @Override
  public void start() throws Exception {



//    On the server side a Vert.x http server can be configured to automatically send back 100
// Continue interim responses when it receives an Expect: 100-Continue header.
//
//            This is done by setting the option setHandle100ContinueAutomatically.
//
//            If you’d prefer to decide whether to send back continue responses manually, then
// this property should be set to false (the default), then you can inspect the headers and call
// writeContinue to have the client continue sending the body:


            HttpServerOptions options = new HttpServerOptions().setHandle100ContinueAutomatically(
                    true);
    vertx.createHttpServer(options).requestHandler(request -> {
      System.out.println("reqeust ..");
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

//      if (request.getHeader("Expect").equalsIgnoreCase("100-Continue")) {
//
//        // Send a 100 continue response
//        request.response().writeContinue();
//
//        // The client should send the body when it receives the 100 response
//        request.bodyHandler(body -> {
//          // Do something with body
//          System.out.println(body);
//        });
//
//        request.endHandler(v -> {
//          request.response().setChunked(true).end(jsonObject.encode());
//        });
//      }

      if (request.getHeader("Expect").equalsIgnoreCase("100-Continue")) {

        //
        boolean rejectAndClose = true;
        if (rejectAndClose) {

          // Reject with a failure code and close the connection
          // this is probably best with persistent connection
          request.response()
                  .setStatusCode(405)
                  .putHeader("Connection", "close")
                  .end();
        } else {

          // Reject with a failure code and ignore the body
          // this may be appropriate if the body is small
          request.response()
                  .setStatusCode(405)
                  .end();
        }
      }

    }).listen(8080, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server is now listening!");
      } else {
        System.out.println("Failed to bind!");
      }
    });
  }
}
