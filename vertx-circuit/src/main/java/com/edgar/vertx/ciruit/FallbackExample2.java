package com.edgar.vertx.ciruit;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Edgar on 2016/7/1.
 *
 * @author Edgar  Date 2016/7/1
 */
public class FallbackExample2 extends AbstractVerticle {
    public static void main(String[] args) {

        new Launcher().execute("run", FallbackExample2.class.getName());
    }
    @Override
    public void start() throws Exception {

        AtomicInteger seq = new AtomicInteger();
        vertx.createHttpServer().requestHandler(req -> {
            System.out.println(seq.incrementAndGet());
            req.response().setStatusCode(400).end();
        }).listen(8080);

        CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
                new CircuitBreakerOptions()
                        .setMaxFailures(5) // number of failure before opening the circuit
                        .setTimeout(2000) // consider a failure if the operation does not succeed in time
                        .setFallbackOnFailure(true) // do we call the fallback on failure
//                        .setResetTimeout(10000) // time spent in open state before attempting to re-try
        );

//        breaker.execute(future -> {
//            // some code executing with the breaker
//            // the code reports failures or success on the given future.
//            // if this future is marked as failed, the breaker increased the
//            // number of failures
//        }).setHandler(ar -> {
//            // Get the operation result.
//        });

        for (int i = 1; i < 100; i++) {
            vertx.setTimer(100* i, l -> {
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
            });
        }

    }
}
