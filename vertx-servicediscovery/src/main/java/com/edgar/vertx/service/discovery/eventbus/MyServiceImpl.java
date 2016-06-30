package com.edgar.vertx.service.discovery.eventbus;

/**
 * Created by Edgar on 2016/6/30.
 *
 * @author Edgar  Date 2016/6/30
 */
public class MyServiceImpl implements MyService {
  @Override
  public void say() {
    System.out.println("proxy");
  }
}
