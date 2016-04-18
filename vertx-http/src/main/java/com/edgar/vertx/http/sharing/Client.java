package com.edgar.vertx.http.sharing;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/9/2.
 */
public class Client extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runExample(Client.class);
    }

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(1000, l -> {
           vertx.createHttpClient().getNow(8080, "localhost", "/", resp -> {
               resp.bodyHandler(body -> {
                   System.out.println(body.toString("ISO-8859-1"));
               });
           }) ;
        });
    }
}
