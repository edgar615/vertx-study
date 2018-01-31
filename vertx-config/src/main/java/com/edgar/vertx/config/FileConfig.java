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
public class FileConfig extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(FileConfig.class.getName());
  }

  @Override
  public void start() throws Exception {

    ConfigStoreOptions fileStore = new ConfigStoreOptions()
            .setType("file")
            .setConfig(new JsonObject().put("path", "my-config.json"));


    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
            .addStore(fileStore)
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

    //file有缓存
    retriever.listen(cc -> {
      System.out.println(cc.getNewConfiguration());
    });

    JsonObject last = retriever.getCachedConfig();
//    System.out.println(last);
  }
}
