package com.edgar.vertx.service.discovery.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar on 16-6-29.
 */
public class HttpServiceVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", HttpServiceVerticle.class.getName());
    }
    ServiceDiscovery discovery;
    Map<String, String> map = new HashMap<>();
    @Override
    public void start() throws Exception {
        //引入vertx-service-discovery-backend-redis之后，所有的ServiceDiscovery都需要使用redis，所以这里先注释
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
//        discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
//                .setBackendConfiguration(new JsonObject().put("host", "10.11.0.31").put("key", "records")));

        //服务关闭之后，redis中的值仍然存在
        Record httpRecord = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
        System.out.println(httpRecord.getRegistration());
        discovery.publish(httpRecord, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                map.put("id", publishedRecord.getRegistration());
                System.out.println(httpRecord.getRegistration());
                System.out.println(publishedRecord.getLocation());
                System.out.println(publishedRecord.getType());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });
        discovery.close();
    }

//    @Override
//    public void stop() throws Exception {
//        if (discovery != null) {
//            String id = map.get("id");
//            System.out.println(id);
//            discovery.unpublish(id, ar -> {
//                if (ar.succeeded()) {
//                    System.out.println("unpublish");
//                    discovery.close();
//                } else {
//                    System.out.println("unpublish failed");
//                    ar.cause().printStackTrace();
//                    discovery.close();
//                }
//            });
//        }
//    }
}
