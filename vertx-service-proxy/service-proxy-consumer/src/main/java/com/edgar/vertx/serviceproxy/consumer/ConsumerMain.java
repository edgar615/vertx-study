package com.edgar.vertx.serviceproxy.consumer;

import com.edgar.vertx.serviceproxy.provider.ProcessorServiceVerticle;
import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2016/11/29.
 *
 * @author Edgar  Date 2016/11/29
 */
public class ConsumerMain {
  public static void main(String[] args) {
    new Launcher().execute("run", ConsumerVerticle.class.getName(), "--cluster");
  }
}
