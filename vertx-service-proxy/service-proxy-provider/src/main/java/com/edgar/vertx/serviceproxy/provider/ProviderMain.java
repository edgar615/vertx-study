package com.edgar.vertx.serviceproxy.provider;

import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2016/11/29.
 *
 * @author Edgar  Date 2016/11/29
 */
public class ProviderMain {
  public static void main(String[] args) {
    new Launcher().execute("run", ProcessorServiceVerticle.class.getName(), "--cluster");
  }
}
