package com.edgar.vertx.http;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

/**
 * 用来做nginx负载均衡测试的.
 */
public class ServerExample2 {

    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(request -> {
            System.out.println(9001);
            request.response().end("hello world!" + 9001);
        }).listen(9001);

    }
}
