package com.edgar.vertx.block;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.util.concurrent.TimeUnit;

/**
 * Created by edgar on 17-3-20.
 */
public class BlockTest extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(BlockTest.class.getName());
  }

  @Override
  public void start() throws Exception {
    vertx.executeBlocking(f -> {
      //block method
      try {
        TimeUnit.MILLISECONDS.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      f.complete("hello");
    }, ar -> {
      System.out.println(System.currentTimeMillis() + ": " + ar.result());
    });

    vertx.executeBlocking(f -> {
      //block method
      try {
        TimeUnit.MILLISECONDS.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      f.complete("hello2");
    }, ar -> {
      System.out.println(System.currentTimeMillis() + ": " + ar.result());
    });

    vertx.executeBlocking(f -> {
      //block method
      try {
        TimeUnit.MILLISECONDS.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      f.complete("hello3");
    }, ar -> {
      System.out.println(System.currentTimeMillis() + ": " + ar.result());
    });
  }
}
