package com.edgar.vertx.ciruit;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2016/7/1.
 *
 * @author Edgar  Date 2016/7/1
 */
public class EventbusNotificationExample extends AbstractVerticle {
  public static void main(String[] args) {

    new Launcher().execute("run", EventbusNotificationExample.class.getName());
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {
      req.response().setStatusCode(400).end();
    }).listen(8080);

    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
                                                   new CircuitBreakerOptions()
                                                           .setMaxFailures(5)
                                                           .setTimeout(1000)
                                                           .setNotificationAddress(
                                                                   "vertx.circuit-breaker")
    );

    vertx.eventBus().consumer("vertx.circuit-breaker", msg -> {
      System.out.println(msg.body());
    });

    vertx.setPeriodic(1000, l -> {
      breaker.<String>executeWithFallback(future -> {
        vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
          if (response.statusCode() != 200) {
            future.fail("HTTP error");
          } else {
            response
                    .exceptionHandler(future::fail)
                    .bodyHandler(buffer -> {
                      future.complete(buffer.toString());
                    });
          }
        });
      }, r -> "Hello").setHandler(ar -> {
        if (ar.succeeded()) {
          System.out.println(ar.result());
        } else {
          System.out.println(ar.cause());
        }
      });
    });
  }
}
