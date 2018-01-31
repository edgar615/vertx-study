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
public class DirectoryConfig extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(DirectoryConfig.class.getName());
  }

  @Override
  public void start() throws Exception {

    ConfigStoreOptions dir = new ConfigStoreOptions()
            .setType("directory")
            .setConfig(new JsonObject().put("path", "H:\\dev\\workspace\\vertx-study\\vertx"
                                                    + "-config\\src\\main\\resources\\config")
                               .put("filesets", new JsonArray()
                                       .add(new JsonObject().put("pattern", "*json"))
                                       .add(new JsonObject().put("pattern", "*.properties")
                                                    .put("format", "properties"))
                               ));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
            .addStore(dir)
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
