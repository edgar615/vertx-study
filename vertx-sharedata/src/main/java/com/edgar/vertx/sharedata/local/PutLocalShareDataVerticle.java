package com.edgar.vertx.sharedata.local;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class PutLocalShareDataVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(PutLocalShareDataVerticle.class);
  }

  @Override
  public void start() throws Exception {
    SharedData sharedData = vertx.sharedData();
    LocalMap<String, String> map1 =   sharedData.getLocalMap("mymap1");
    map1.put("foo", "bar");
    vertx.deployVerticle("com.edgar.vertx.sharedata.local.GetLocalShareDataVerticle");
  }
}
