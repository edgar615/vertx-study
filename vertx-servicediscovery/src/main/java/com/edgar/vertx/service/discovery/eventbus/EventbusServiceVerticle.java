package com.edgar.vertx.service.discovery.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Created by edgar on 16-6-29.
 */
public class EventbusServiceVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    new Launcher().execute("run", EventbusServiceVerticle.class.getName(), "--cluster");
  }

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
    Record record = EventBusService.createRecord(
            "some-eventbus-service", // The service name
            "address", // the service address,
            "com.edgar.vertx.service.discovery.eventbus.MyService", // the service interface as string
            new JsonObject()
                    .put("some-metadata", "some value")
    );

    MyService myService = new MyServiceImpl();
    ProxyHelper.registerService(MyService.class, vertx, myService, "address");

    discovery.publish(record, ar -> {
      if (ar.succeeded()) {
        Record publishedRecord = ar.result();
        System.out.println(publishedRecord.getLocation());
        System.out.println(publishedRecord.getType());
        System.out.println(publishedRecord.getStatus());
      } else {

      }
    });

    discovery.close();
  }
}
