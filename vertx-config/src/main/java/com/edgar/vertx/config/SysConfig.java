package com.edgar.vertx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2018/1/31.
 *
 * @author Edgar  Date 2018/1/31
 */
public class SysConfig extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(SysConfig.class.getName());
  }

  @Override
  public void start() throws Exception {

    ConfigStoreOptions json = new ConfigStoreOptions()
            .setType("sys")
            .setConfig(new JsonObject().put("cache", false));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
            .addStore(json)
            .setScanPeriod(2000);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig(ar -> {
      if (ar.failed()) {
        // Failed to retrieve the configuration
        System.out.println("error");
      } else {
        JsonObject config = ar.result();
        System.out.println(config);
      }
    });

    retriever.listen(cc -> {
      System.out.println(cc.getNewConfiguration());
    });
  }
}
