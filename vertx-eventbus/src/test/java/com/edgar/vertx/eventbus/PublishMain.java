package com.edgar.vertx.eventbus;

import com.edgar.vertx.eventbus.PublishVerticle;
import com.edgar.vertx.eventbus.SendVerticle;
import com.edgar.vertx.util.Runner;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class PublishMain {
  public static void main(String[] args) {
    Runner.runExample(PublishVerticle.class);
  }
}
