package com.edgar.vertx.http.continue100;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientRequest;

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
//    100-Continue handling
//
//    According to the HTTP 1.1 specification a client can set a header Expect: 100-Continue and
// send the request header before sending the rest of the request body.
//
//            The server can then respond with an interim response status Status: 100 (Continue)
// to signify to the client that it is ok to send the rest of the body.
//
//            The idea here is it allows the server to authorise and accept/reject the request
// before large amounts of data are sent. Sending large amounts of data if the request might not
// be accepted is a waste of bandwidth and ties up the server in reading data that it will just
// discard.
//
//    Vert.x allows you to set a continueHandler on the client request object
//
//    This will be called if the server sends back a Status: 100 (Continue) response to signify
// that it is ok to send the rest of the request.
//
//            This is used in conjunction with `sendHead`to send the head of the request.
    HttpClientRequest request = vertx.createHttpClient().put(8080, "localhost", "/", response
            -> {
      System.out.print(response.statusCode());
      response.bodyHandler(
              body -> System.out.println(body.toJsonObject().encodePrettily()));
    });

    request.putHeader("Expect", "100-Continue");

    request.continueHandler(v -> {
      // OK to send rest of body
      System.out.println("continue");
      request.write("Some data");
      request.write("Some more data");
      request.end();
    });

  }
}
