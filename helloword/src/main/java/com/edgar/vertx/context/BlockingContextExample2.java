package com.edgar.vertx.context;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/5/3.
 *
 * @author Edgar  Date 2017/5/3
 */
public class BlockingContextExample2 extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(BlockingContextExample2.class.getName());
  }

  @Override
  public void start() throws Exception {
    Handler<Future<String>> blockingCodeHandler = future -> {
      throw new RuntimeException();
    };

    Handler<AsyncResult<String>> resultHandler = result -> {
      if (result.succeeded()) {
        System.out.println("Got result");
      } else {
        System.out.println("Blocking code failed");
        result.cause().printStackTrace(System.out);
      }
    };

    vertx.executeBlocking(blockingCodeHandler, resultHandler);
//    vertx.runOnContext(v -> {
//
//      // On the event loop
//      System.out.println("Calling blocking block from " + Thread.currentThread());
//
//      Handler<Future<String>> blockingCodeHandler = future -> {
//        // Non event loop
//        System.out.println("Computing with " + Thread.currentThread());
//        future.complete("some result");
//      };
//
//      Handler<AsyncResult<String>> resultHandler = result -> {
//        // Back to the event loop
//        System.out.println("Got result in " + Thread.currentThread());
//      };
//
//      // Execute the blocking code handler and the associated result handler
//      vertx.executeBlocking(blockingCodeHandler, resultHandler);
//    });
  }
}
