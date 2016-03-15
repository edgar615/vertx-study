package com.edgar.vertx.http.pump;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;

/**
 * Created by Administrator on 2015/9/2.
 */
public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(Server.class);
    }

    @Override
    public void start() throws Exception {
      vertx.createHttpServer().requestHandler(request -> {
        HttpServerResponse response = request.response();
        if (request.method() == HttpMethod.PUT) {
          response.setChunked(true);
          Pump.pump(request, response).start();
          request.endHandler(v -> response.end());
        } else {
          response.setStatusCode(400).end();
        }
      }).listen(8080);
    }
}
