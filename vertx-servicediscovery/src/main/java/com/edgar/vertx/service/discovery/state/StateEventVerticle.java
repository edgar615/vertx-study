package com.edgar.vertx.service.discovery.state;

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
public class StateEventVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", StateEventVerticle.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
    vertx.eventBus().consumer("vertx.discovery.announce", msg -> {
      System.out.println("announce: " + msg.body());
    });

    vertx.eventBus().consumer("vertx.discovery.usage", msg -> {
      System.out.println("usage: " + msg.body());
    });

    vertx.setPeriodic(5000, l -> {
      discovery.getRecords(r -> true, ar -> {
        List<Record> records = ar.result();
        for (Record record : records) {
          ServiceReference serviceReference = discovery.getReference(record);
          HttpClient httpClient = serviceReference.get();
          serviceReference.release();
        }
      });
    });
    discovery.close();
  }
}
