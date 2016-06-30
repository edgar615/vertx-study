package com.edgar.vertx.service.discovery.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

/**
 * Created by edgar on 16-6-29.
 */
public class JdbcClientVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", JdbcClientVerticle.class.getName(), "--cluster");
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        discovery.getRecord(new JsonObject().put("name", "some-data-source-service"), ar -> {
            if (ar.succeeded() && ar.result() != null) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getLocation());
                System.out.println(publishedRecord.getType());
                System.out.println(publishedRecord.getStatus());
                // Retrieve the service reference
                ServiceReference reference = discovery.getReferenceWithConfiguration(
                        ar.result(), // The record
                        new JsonObject()); // Some additional metadata.put("user", "admin").put("password", "csst")

                // Retrieve the service object
                JDBCClient client = reference.get();
                client.getConnection(res -> {
                    if (res.succeeded()) {
                        System.out.println("connect");
                        SQLConnection connection = res.result();

                        connection.query("SELECT * FROM user limit 10", res2 -> {
                            if (res2.succeeded()) {

                                ResultSet rs = res2.result();
                                System.out.println(rs.getResults());
                            }
                            // when done
                            reference.release();
                        });
                    } else {
                        // Failed to get connection - deal with it
                        System.out.println("connect failed");
                        res.cause().printStackTrace();
                    }
                });
            }
        });
        discovery.close();
    }
}
