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
public class RetryExample2 extends AbstractVerticle {
  public static void main(String[] args) {

    new Launcher().execute("run", RetryExample2.class.getName());
  }

  @Override
  public void start() throws Exception {

    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
                                                   new CircuitBreakerOptions()
                                                           .setMaxFailures(5)
                                                           .setTimeout(1000)
                                                           .setResetTimeout(3000)
                                                           .setMaxRetries(3)
    )
            .openHandler(v -> {
              System.out.println(System.currentTimeMillis() + " : Circuit opened");
            }).closeHandler(v -> {
              System.out.println(System.currentTimeMillis() + " : Circuit closed");
            }).halfOpenHandler(v -> {
              System.out.println(System.currentTimeMillis() + " : reset (half-open state)");
            });

//        breaker.execute(future -> {
//            // some code executing with the breaker
//            // the code reports failures or success on the given future.
//            // if this future is marked as failed, the breaker increased the
//            // number of failures
//        }).setHandler(ar -> {
//            // Get the operation result.
//        });
    AtomicInteger seq = new AtomicInteger();
    vertx.setPeriodic(1000, l -> {
      final long time = System.currentTimeMillis();
      final int i = seq.incrementAndGet();
      breaker.<String>execute(future -> {
        //do nothing
        System.out.println(System.currentTimeMillis() + " execute:" + i);
      }).setHandler(ar -> {
        if (ar.succeeded()) {
          System.out.println(time + " : OK: "
                             + ar.result() + " " + i);
        } else {
          System.out.println(
                  time + " : ERROR: " + ar.cause() + " " + i);
        }
      });
    });

  }
}
