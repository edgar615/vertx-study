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
                        .setMaxFailures(5) // number of failure before opening the circuit
                        .setTimeout(2000) // consider a failure if the operation does not succeed in time
                        .setFallbackOnFailure(true) // do we call the fallback on failure
//                        .setResetTimeout(10000) // time spent in open state before attempting to re-try
//                .setNotificationAddress()
        ).fallback(t -> "HELLO")
                .openHandler(v -> {
                    System.out.println("Circuit opened");
                }).closeHandler(v -> {
                    System.out.println("Circuit closed");
                }).halfOpenHandler(v -> {
                    System.out.println("reset (half-open state)");
                });

        vertx.eventBus().consumer("vertx.circuit-breaker", msg -> {
            System.out.println(msg.body());
        });

        for (int i = 0; i < 10; i++) {
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
            }, r -> "FALLBACK").setHandler(ar -> {
                if (ar.succeeded()) {
                    System.out.println(ar.result());
                } else {
                    System.out.println(ar.cause());
                }
            });
        }
    }
}
