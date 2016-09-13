package com.edgar.vertx.http;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;

/**
 * 用来做nginx负载均衡测试的.
 */
public class ServerExample1 {

    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(request -> {
            System.out.println(9000);
            request.response().end("hello world!" + 9000);
        }).listen(9000);

    }
}
