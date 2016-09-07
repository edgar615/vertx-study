package com.edgar.vertx.launcher;

import io.vertx.core.AbstractVerticle;

/**
 * Created by Edgar on 2016/9/6.
 *
 * @author Edgar  Date 2016/9/6
 */
public class Server extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        String msg = config().getString("test", "HELLO");
        vertx.createHttpServer().requestHandler(req -> {
            req.response().end(msg);
        }).listen(8080);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stop");
    }
}
