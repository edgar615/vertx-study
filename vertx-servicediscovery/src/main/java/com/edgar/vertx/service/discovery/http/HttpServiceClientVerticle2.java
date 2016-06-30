package com.edgar.vertx.service.discovery.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * Created by edgar on 16-6-29.
 */
public class HttpServiceClientVerticle2 extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", HttpServiceClientVerticle2.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    HttpEndpoint.getClient(discovery, new JsonObject().put("name", "some-http-service"), ar -> {
      if (ar.succeeded()) {
        HttpClient client = ar.result();

        // You need to path the complete path
        client.getNow("/api/persons", response -> {

          // ...

          // Dont' forget to release the service
          ServiceDiscovery.releaseServiceObject(discovery, client);

        });
      }
    });
    discovery.close();
  }
}
