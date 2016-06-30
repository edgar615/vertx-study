package com.edgar.vertx.service.discovery.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

/**
 * Created by edgar on 16-6-29.
 */
public class EventbusClientVerticle2 extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", EventbusClientVerticle2.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    EventBusService.getProxy(discovery, MyService.class, ar -> {
      if (ar.succeeded()) {
        MyService myService = ar.result();
        myService.say();
// Dont' forget to release the service
        ServiceDiscovery.releaseServiceObject(discovery, myService);
      }

    });
    discovery.close();
  }
}
