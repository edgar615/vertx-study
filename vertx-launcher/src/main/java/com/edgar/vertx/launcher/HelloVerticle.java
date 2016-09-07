package com.edgar.vertx.launcher;

import io.vertx.core.AbstractVerticle;

/**
 * Usage: java -jar vertx-launcher-1.0-SNAPSHOT.jar [COMMAND] [OPTIONS] [arg...]
 * <p/>
 * Commands:
 * bare         Creates a bare instance of vert.x.
 * list         List vert.x applications java -jar vertx-launcher-1.0-SNAPSHOT.jar list
 * my-command   A simple hello command.
 * run          Runs a verticle called <main-verticle> in its own instance of vert.x.
 * start        Start a vert.x application in background java -jar vertx-launcher-1.0-SNAPSHOT.jar start --conf=config.json
 * stop         Stop a vert.x application java -jar vertx-launcher-1.0-SNAPSHOT.jar stop 690d4381-0981-4dba-b51c-c21f1162570f
 * version      Displays the version.
 * <p/>
 * Run 'java -jar vertx-launcher-1.0-SNAPSHOT.jar COMMAND --help' for more
 * information on a command.
 *
 * @author Edgar  Date 2016/3/16
 */
public class HelloVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("starting");
    }
}
