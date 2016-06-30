package com.edgar.vertx.service.discovery.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

import java.util.List;

/**
 * Created by edgar on 16-6-29.
 */
public class HttpServiceClientVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", HttpServiceClientVerticle.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    discovery.getRecords(r -> true, ar-> {
      List<Record> records = ar.result();
      for (Record record: records) {
        System.out.println(record.getType());
        System.out.println(record.getLocation());
        ServiceReference serviceReference = discovery.getReference(record);
        HttpClient httpClient = serviceReference.get();
        // You need to path the complete path
        httpClient.getNow("/api/persons", response -> {

          // ...

          // Dont' forget to release the service
          serviceReference.release();

        });
      }
    });
    discovery.close();
  }
}
