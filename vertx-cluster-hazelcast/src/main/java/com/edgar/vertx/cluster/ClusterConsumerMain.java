package com.edgar.vertx.cluster;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by Edgar on 2016/3/11.
 *
 * @author Edgar  Date 2016/3/11
 */
public class ClusterConsumerMain {
  public static void main(String[] args) {

    ClusterManager mgr = new HazelcastClusterManager();

    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(new AbstractVerticle() {
          @Override
          public void start() throws Exception {
            vertx.eventBus().consumer("nocluster", msg -> {
              System.out.println(msg.body());
            });
          }
        });
      } else {
        // failed!
      }
    });

  }
}
