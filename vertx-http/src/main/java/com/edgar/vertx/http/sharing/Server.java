package com.edgar.vertx.http.sharing;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * Created by Administrator on 2015/9/2.
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    //When several HTTP servers listen on the same port, vert.x orchestrates the request handling
    // using a round-robin strategy.
    vertx.deployVerticle(
            "com.edgar.vertx.http.sharing.HttpServerVerticle",
            new DeploymentOptions().setInstances(2)
    );
  }
}
