package com.edgar.vertx.hostname;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by Edgar on 2017/11/23.
 *
 * @author Edgar  Date 2017/11/23
 */
public class HostnameExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(HostnameExample.class.getName());
  }

  @Override
  public void start() throws Exception {
  }
}
