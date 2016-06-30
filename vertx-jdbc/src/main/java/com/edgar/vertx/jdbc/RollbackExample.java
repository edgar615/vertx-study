package com.edgar.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

/**
 * Created by Administrator on 2015/9/6.
 */
public class RollbackExample extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(RollbackExample.class.getName());
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
            execute(conn.result(), "create table test(id int primary key, name varchar(255))", create -> {

                startTx(conn.result(), beginTrans -> {
                    execute(conn.result(), "insert into test value(1, 'hello')", insert -> {
                        rollback(conn.result(), commitTrans -> {
                            query(conn.result(), "select count(*) from test", rs -> {
                                for (JsonArray line : rs.getResults()) {
                                    System.out.println(line.encode());
                                }

                                conn.result().close(done -> {
                                    if (done.failed()) {
                                        throw new RuntimeException(done.cause());
                                    }
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    private void execute(SQLConnection conn, String sql, Handler<Void> done) {
        conn.execute(sql, res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }
            done.handle(null);
        });
    }

    private void query(SQLConnection conn, String sql, Handler<ResultSet> done) {
        conn.query(sql, res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }
            done.handle(res.result());
        });
    }

    private void startTx(SQLConnection conn, Handler<ResultSet> done) {
        conn.setAutoCommit(false, res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }

            done.handle(null);
        });
    }

    private void rollback(SQLConnection conn, Handler<ResultSet> done) {
        conn.rollback(res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }
            done.handle(null);
        });
    }
}