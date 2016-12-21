package com.edgar.vertx.helloword;

import io.vertx.core.Vertx;

public class Server {
  public static void main(String[] args) {
    Vertx.vertx().createHttpServer().requestHandler(req -> {
      req.response()
              .putHeader("content-type", "text/plain")
              .end("Hello from Vert.x!");
    }).listen(8080);
  }
}
