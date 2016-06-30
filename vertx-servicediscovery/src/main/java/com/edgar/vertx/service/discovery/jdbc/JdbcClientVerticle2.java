package com.edgar.vertx.service.discovery.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.JDBCDataSource;

/**
 * Created by edgar on 16-6-29.
 */
public class JdbcClientVerticle2 extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", JdbcClientVerticle2.class.getName(), "--cluster");
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        JDBCDataSource.<JsonObject>getJDBCClient(discovery,
                new JsonObject().put("name", "some-data-source-service"),
                new JsonObject(), // Some additional metadata
                ar -> {
                    if (ar.succeeded()) {
                        JDBCClient client = ar.result();

                        client.getConnection(res -> {
                            if (res.succeeded()) {
                                System.out.println("connect");
                                SQLConnection connection = res.result();

                                connection.query("SELECT * FROM user limit 10", res2 -> {
                                    if (res2.succeeded()) {

                                        ResultSet rs = res2.result();
                                        System.out.println(rs.getResults());
                                    }
                                    // Dont' forget to release the service
                                    ServiceDiscovery.releaseServiceObject(discovery, client);
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
