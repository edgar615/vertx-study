package com.edgar.vertx.sync.simple;

import static io.vertx.ext.sync.Sync.awaitEvent;

import co.paralleluniverse.fibers.Suspendable;
import com.edgar.util.vertx.runner.Runner;
import io.vertx.ext.sync.SyncVerticle;

public class Server extends SyncVerticle {

  /*
  Convenience method so you can run it in your IDE
  Note if in IDE need to edit run settings and add:
  -javaagent:/home/tim/.m2/repository/co/paralleluniverse/quasar-core/0.7.2/quasar-core-0.7.2.jar
   */
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Suspendable
  @Override
  public void start() throws Exception {

    // Wait for a single event - this just demonstrates that you can wait for single events
    System.out.println("Waiting for single event");
    long tid = awaitEvent(h -> vertx.setPeriodic(1000, h));
    System.out.println("Single event has fired");


  }

}