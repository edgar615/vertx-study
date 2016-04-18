package com.edgar.vertx.sharedata.lock;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class LockVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(LockVerticle.class);
  }

  @Override
  public void start() throws Exception {
    SharedData sharedData = vertx.sharedData();
    sharedData.<String, String>getClusterWideMap("mymap", res -> {
      if (res.succeeded()) {
        AsyncMap<String, String> map = res.result();
        map.put("foo", "bar", resPut -> {
          if (resPut.succeeded()) {
            // Successfully put the value
          } else {
            // Something went wrong!
          }
        });
      } else {
        // Something went wrong!
      }
    });

//    It’s often useful to maintain an atomic counter across the different nodes of your applicatio
//    sd.getCounter("mycounter", res -> {
//      if (res.succeeded()) {
//        Counter counter = res.result();
//      } else {
//        // Something went wrong!
//      }
//    });
  }
}
