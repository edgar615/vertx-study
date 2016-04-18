package com.edgar.vertx.http.upload;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.Pump;

import java.util.UUID;

/**
 * Created by Administrator on 2015/9/2.
 */
public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(Server.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.pause();
            String filename = "d:/" +  UUID.randomUUID() + ".uploaded";
            vertx.fileSystem().open(filename, new OpenOptions(), ares -> {
              AsyncFile file = ares.result();
              Pump pump = Pump.pump(req, file);
                req.endHandler(v1 -> file.close(v2 -> {
                    System.out.println("Uploaded to " + filename);
                    req.response().end();
                }));
                pump.start();
                req.resume();
            });
        }).listen(8080);
    }
}
