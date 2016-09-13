package com.edgar.vertx.launcher;

import io.vertx.core.AbstractVerticle;

/**
 * Usage: java -jar vertx-launcher-1.0-SNAPSHOT.jar [COMMAND] [OPTIONS] [arg...]
 * <p/>
 * Commands:
 * bare         Creates a bare instance of vert.x.
 * list         List vert.x applications java -jar vertx-launcher-1.0-SNAPSHOT.jar list
 * my-command   A simple hello command.
 * run          Runs a verticle called <main-verticle> in its own instance of vert.x. java -jar vertx-launcher-1.0- SNAPSHOT.jar run com.edgar.vertx.launcher.HelloVerticle
 * start        Start a vert.x application in background java -jar vertx-launcher-1.0- SNAPSHOT.jar start --conf=config.json 必须指定Main-Verticle
 * stop         Stop a vert.x application java -jar vertx-launcher-1.0-SNAPSHOT.jar stop 690d4381-0981-4dba-b51c-c21f1162570f
 * version      Displays the version.
 * <p/>
 * Run 'java -jar vertx-launcher-1.0-SNAPSHOT.jar COMMAND --help' for more
 * information on a command.
 *java -jar vertx-launcher-1.0-SNAPSHOT.jar hello --name=edgar
 * @author Edgar  Date 2016/3/16
 */
public class HelloVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        String msg = config().getString("test", "HELLO");
        System.out.println("starting");
        vertx.createHttpServer().requestHandler(req -> {
            req.response().end(msg);
        }).listen(8080);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stop...");
    }
}
