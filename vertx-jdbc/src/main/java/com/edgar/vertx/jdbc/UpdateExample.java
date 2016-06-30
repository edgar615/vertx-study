package com.edgar.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

/**
 * Created by Administrator on 2015/9/6.
 */
public class UpdateExample extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(UpdateExample.class.getName());
    }

    @Override
    public void start() throws Exception {
        final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("url", "jdbc:mysql://localhost:3306/fire")
                .put("max_pool_size", 30)
                .put("user", "root"));

        client.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }
            final SQLConnection connection = conn.result();
            connection.execute("create table test(id int primary key, name varchar(255))", res -> {
                if (res.failed()) {
                    throw new RuntimeException(res.cause());
                }
            });
            connection.execute("insert into test value(1, 'hello'), (2, 'world')", insert -> {

                connection.update("update test set name = concat(name, '-')", update -> {
                    UpdateResult updateResult = update.result();
                    System.out.println("No. of rows updated: " + updateResult.getUpdated());
                });

                connection.updateWithParams("update test set name = concat(name, '-') where id = ?", new JsonArray().add(1), update -> {
                    UpdateResult updateResult = update.result();
                    System.out.println("No. of rows updated: " + updateResult.getUpdated());
                });

            });
        });
    }
}