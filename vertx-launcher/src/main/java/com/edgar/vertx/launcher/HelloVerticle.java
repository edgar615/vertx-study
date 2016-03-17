package com.edgar.vertx.launcher;

import io.vertx.core.AbstractVerticle;

/**
 * Usage: java -jar vertx-launcher-1.0-SNAPSHOT.jar [COMMAND] [OPTIONS] [arg...]

 Commands:
 bare         Creates a bare instance of vert.x.
 list         List vert.x applications
 my-command   A simple hello command.
 run          Runs a verticle called <main-verticle> in its own instance of
 vert.x.
 start        Start a vert.x application in background
 stop         Stop a vert.x application
 version      Displays the version.

 Run 'java -jar vertx-launcher-1.0-SNAPSHOT.jar COMMAND --help' for more
 information on a command.
 *
 * @author Edgar  Date 2016/3/16
 */
public class HelloVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    System.out.println("starting");
  }
}
