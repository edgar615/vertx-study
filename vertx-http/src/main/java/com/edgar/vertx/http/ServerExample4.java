package com.edgar.vertx.http;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;

/**
 * Created by Administrator on 2015/9/1.
 */
public class ServerExample4 {

    public static void main(String[] args) {

        //Handling requests
        //The default host is 0.0.0.0 which means 'listen on all available addresses' and the default port is 80.
        Vertx.vertx().createHttpServer().requestHandler(request -> {
            request.response().end("80: hello world!");
        }).listen();

        Vertx.vertx().createHttpServer().requestHandler(request -> {
            request.response().end("8080: hello world!");
        }).listen(8080);
    }
}
