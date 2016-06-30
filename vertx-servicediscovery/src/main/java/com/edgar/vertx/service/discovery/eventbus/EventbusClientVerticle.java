package com.edgar.vertx.service.discovery.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

/**
 * Created by edgar on 16-6-29.
 */
public class EventbusClientVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", EventbusClientVerticle.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    discovery.getRecord(new JsonObject().put("name", "some-eventbus-service"), ar -> {
      Record record = ar.result();
      System.out.println(record.getType());
      System.out.println(record.getLocation());
      ServiceReference reference = discovery.getReference(record);
      MyService myService = reference.get();
      myService.say();

      reference.release();
    });
    discovery.close();
  }
}
