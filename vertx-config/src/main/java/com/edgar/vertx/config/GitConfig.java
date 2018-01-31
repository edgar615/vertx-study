package com.edgar.vertx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2018/1/31.
 *
 * @author Edgar  Date 2018/1/31
 */
public class GitConfig extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(GitConfig.class.getName());
  }

  @Override
  public void start() throws Exception {

    ConfigStoreOptions git = new ConfigStoreOptions()
            .setType("git")
            .setConfig(new JsonObject()
                               .put("url", "https://github.com/edgar615/config-test.git")
                               .put("path", "local")
                               .put("filesets",
                                    new JsonArray().add(new JsonObject().put("pattern", "*.json"))));


    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
            .addStore(git)
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
