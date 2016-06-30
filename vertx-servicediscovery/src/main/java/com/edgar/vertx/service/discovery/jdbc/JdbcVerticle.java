package com.edgar.vertx.service.discovery.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.JDBCDataSource;

/**
 * Created by edgar on 16-6-29.
 */
public class JdbcVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        new Launcher().execute("run", JdbcVerticle.class.getName(), "--cluster");
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        Record record = JDBCDataSource.createRecord(
                "some-data-source-service", // The service name
                new JsonObject()//.put("url", "10.4.7.48:3306/user")
                        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
                        .put("driverClassName", "com.mysql.jdbc.Driver")
                        .put("jdbcUrl", "jdbc:mysql://10.4.7.48:3306/user")
                        .put("initial_pool_size", 0)
                        .put("minimumIdle", 0)
                        .put("username", "admin").put("password", "csst"), // The location
                new JsonObject().put("some-metadata", "some-value") // Some metadata
        );

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                Record publishedRecord = ar.result();
                System.out.println(publishedRecord.getLocation());
                System.out.println(publishedRecord.getType());
                System.out.println(publishedRecord.getStatus());
            } else {

            }
        });

//        discovery.close();
    }
}
