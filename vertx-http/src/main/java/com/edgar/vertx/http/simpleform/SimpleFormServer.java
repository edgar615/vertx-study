package com.edgar.vertx.http.simpleform;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/9/1.
 */
public class SimpleFormServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SimpleFormServer.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(request -> {
           if (request.path().equals("/")) {
               request.response().sendFile("index.html");
           } else if (request.path().startsWith("/form")) {
               request.response().setChunked(true);
               request.setExpectMultipart(true);
               request.endHandler(v -> {
                   for (String attr : request.formAttributes().names()) {
                       request.response().write("Got attr " + attr + " : " + request.formAttributes().get(attr) + "\n");
                   }
                   request.response().end();
               });
           } else {
               request.response().setStatusCode(404).end();
           }
        }).listen(8080);
    }
}
