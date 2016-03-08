package com.edgar.vertx.helloword;

import com.edgar.vertx.unit.Runner;
import io.vertx.core.Starter;
import io.vertx.core.Vertx;

/**
 * Created by edgar on 16-2-21.
 */
public class Main {
  public static void main(String[] args) {
    Runner.runExample(MyFirstVerticle.class);
  }
}
