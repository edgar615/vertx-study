package com.edgar.vertx.mysql;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.List;

/**
 * Created by Edgar on 2016/3/7.
 *
 * @author Edgar  Date 2016/3/7
 */
public class AsynTest {
  public static void main(String[] args) {
//Using default shared pool
    Vertx vertx = Vertx.vertx();

//    host
//    The host of the database. Defaults to localhost.
//            port
//    The port of the database. Defaults to 5432 for PostgreSQL and 3306 for MySQL.
//            maxPoolSize
//    The number of connections that may be kept open. Defaults to 10.
//    username
//    The username to connect to the database. Defaults to postgres for PostgreSQL and root for MySQL.
//            password
//    The password to connect to the database. Default is not set, i.e. it uses no password.
//    database
//    The name of the database you want to connect to. Defaults to test.


            JsonObject mySQLClientConfig = new JsonObject().put("host", "10.4.7.48").put
                    ("username", "admin").put("password", "csst").put("database", "task");
    AsyncSQLClient sqlClient = MySQLClient.createShared(vertx, mySQLClientConfig);

//    Specifying a pool name
//    AsyncSQLClient mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig, "MySQLPool1");

//    Creating a client with a non shared data source
//    AsyncSQLClient mySQLClient = MySQLClient.createNonShared(vertx, mySQLClientConfig);

    sqlClient.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection sqlConnection = res.result();
        sqlConnection.query("select * from task limit 5", result -> {
          if (result.succeeded()) {
            ResultSet resultSet = result.result();
            List<String> columnNames = resultSet.getColumnNames();
            List<JsonArray> results = resultSet.getResults();
            for (JsonArray row: results) {
              System.out.println(row);
            }
          }
        });
      } else {
        System.err.println("error:" + res.cause());
      }
    });

    sqlClient.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection sqlConnection = res.result();
        sqlConnection.updateWithParams("insert into task(title) values(?)", new JsonArray
                                      ().add("haha"),  result -> {
                                         if (result.succeeded()) {
                                           UpdateResult updateResult = result.result();
                                           System.out.println("No. of rows updated: " + updateResult.getUpdated());
                                           System.out.println("Keys " +
                                                              updateResult.getKeys());
                                         }
                                       });
      } else {
        System.err.println("error:" + res.cause());
      }
    });
  }
}
