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
public class EventbusConfig extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(EventbusConfig.class.getName());
  }

  @Override
  public void start() throws Exception {

    vertx.eventBus().consumer("address-getting-the-conf", msg -> {
      msg.reply(new JsonObject().put("foo", "bar"));
    });

    ConfigStoreOptions eb = new ConfigStoreOptions()
            .setType("event-bus")
            .setConfig(new JsonObject()
                               .put("address", "address-getting-the-conf")
            );

    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
            .addStore(eb)
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
