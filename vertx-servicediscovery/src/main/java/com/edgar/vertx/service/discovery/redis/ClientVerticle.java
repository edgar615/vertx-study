package com.edgar.vertx.service.discovery.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;

/**
 * Created by edgar on 16-6-29.
 */
public class ClientVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", ClientVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
    .setBackendConfiguration(new JsonObject().put("host", "10.11.0.31").put("key", "records")));

      discovery.getRecords(r -> true, ar -> {
          List<Record> records = ar.result();
          for (Record record : records) {
              System.out.println(record.getType());
              System.out.println(record.getLocation());
          }
      });

    discovery.close();
  }
}
