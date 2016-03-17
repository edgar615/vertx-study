package com.edgar.vertx.web.router;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;

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
    vertx.createHttpClient().get(8080, "localhost", "/consume", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Content-Type", "text/html").end();

    vertx.createHttpClient().get(8080, "localhost", "/consume", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Content-Type", "appliction/json").end();

    vertx.createHttpClient().get(8080, "localhost", "/text", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Content-Type", "text/html").end();

    vertx.createHttpClient().get(8080, "localhost", "/text", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Content-Type", "text/json").end();

    //The HTTP accept header is used to signify which MIME types of the response are acceptable
    // to the client.
    //An accept header can have multiple MIME types separated by ‘,’.
    //MIME types can also have a q value appended to them* which signifies a weighting to apply
    // if more than one response MIME type is available matching the accept header. The q value
    // is a number between 0 and 1.0. If omitted it defaults to 1.0.

    //Accept: text/plain
    //Accept: text/plain, text/html
    //Accept: text/plain; q=0.9, text/html With the following the client will accept text/plain
    // or text/html but prefers text/html as it has a higher q value (the default value is q=1.0)

    vertx.createHttpClient().get(8080, "localhost", "/json", response -> {
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Accept", "application/json").end();

    vertx.createHttpClient().get(8080, "localhost", "/json2", response -> {
      System.out.print(response.statusCode());
      System.out.print(response.getHeader("Content-Type"));
      response.bodyHandler(body -> System.out.println(body.toString()));
    }).putHeader("Accept", "application/json; q=0.7, text/html").end();
  }
}
