package com.edgar.vertx.service.discovery.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * Created by edgar on 16-6-29.
 */
public class HttpServiceVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", HttpServiceVerticle.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    Record httpRecord = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
    System.out.println(httpRecord.getRegistration());
    discovery.publish(httpRecord, ar -> {
      if (ar.succeeded()) {
        Record publishedRecord = ar.result();
        System.out.println(httpRecord.getRegistration());
        System.out.println(publishedRecord.getLocation());
        System.out.println(publishedRecord.getType());
        System.out.println(publishedRecord.getStatus());
      } else {

      }
    });

    discovery.close();
  }
}
