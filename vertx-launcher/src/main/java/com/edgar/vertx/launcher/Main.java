package com.edgar.vertx.launcher;

import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2016/4/21.
 *
 * @author Edgar  Date 2016/4/21
 */
public class Main {
  public static void main(String[] args) {
    //    Starter.runCommandLine("run com.csst.microservice.api.verticle.Server -conf D:/csst/iotp2"
//                           + "/core/api-gateway/api-gateway-dispatcher/target/config.json");
    new Launcher().execute("run", "com.csst.microservice.api.verticle.Server",
                           "--conf=target/config.json");
  }
}
