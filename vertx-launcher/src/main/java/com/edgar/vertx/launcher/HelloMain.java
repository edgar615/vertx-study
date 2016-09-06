package com.edgar.vertx.launcher;

import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2016/4/21.
 *
 * @author Edgar  Date 2016/4/21
 */
public class HelloMain {
  public static void main(String[] args) {
    new Launcher().execute("hello", "--name=vert.x");
  }
}
