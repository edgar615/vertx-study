package com.edgar.vertx.http.gzip;

import com.edgar.util.vertx.runner.Runner;
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
    //no Compression
    vertx.createHttpClient().get(8080, "localhost", "/", response -> {
      System.out.print(response.headers().get("Content-Encoding"));
      System.out.print(response.statusCode());
      response.bodyHandler(body -> System.out.println(body.toJsonObject().encodePrettily()));
    }).end();

    //Compression
    vertx.createHttpClient(new HttpClientOptions().setTryUseCompression(false)).get(8080,
                                                                                   "localhost", "/",
                                                                                   response -> {
                                                                                     System.out
                                                                                             .print(response.headers()
                                                                                                            .names());
                                                                                     System.out
                                                                                             .print(response.headers()
                                                                                                            .getAll("Content-Encoding"));
                                                                                     System.out
                                                                                             .print(response.headers()
                                                                                                            .getAll("Transfer-Encoding"));
                                                                                     System.out
                                                                                             .print(response.statusCode());
                                                                                     response.bodyHandler(
                                                                                             body -> System.out
                                                                                                     .println(
                                                                                                             body.toJsonObject()
                                                                                                                     .encodePrettily()));
                                                                                   }).putHeader(
            "Accept-Encoding", "gzip").end();
    //.putHeader("Accept-Encoding", "deflate")

  }
}
