package com.edgar.vertx.http.sendfile;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/9/1.
 */
public class SendFile extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SendFile.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(request -> {
            String filenname = null;
           if (request.path().equals("/")) {
               filenname = "index.html";
           } else if (request.path().equals("/page1.html")) {
               filenname = "page1.html";
           } else if (request.path().equals("/page2.html")) {
               filenname = "page2.html";
           } else {
               request.response().setStatusCode(404).end();
           }
            if (filenname != null) {
                request.response().sendFile(filenname);
            }
        }).listen(8080);
    }
}
