package com.edgar.vertx.http;

import io.vertx.core.Vertx;

/**
 * 用来做nginx负载均衡测试的.
 */
public class ServerExample3 {

    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(request -> {
            System.out.println(9003);
            request.response().end("hello world!" + 9003);
        }).listen(9003);

    }
}
