package com.edgar.vertx.sharedata.local;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class GetLocalShareDataVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    SharedData sharedData = vertx.sharedData();
    LocalMap<String, String> map1 =   sharedData.getLocalMap("mymap1");
    System.out.println(map1.get("foo"));
  }
}
