package com.edgar.vertx.file;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class FileSystemExample extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(FileSystemExample.class);
  }

  @Override
  public void start() throws Exception {
    vertx.fileSystem().copy("d:/foo.txt", "d:/bar.txt", res -> {
      if (res.succeeded()) {
        System.out.println("copy success");
      } else {
        System.err.println("copy failed" + res.cause().getMessage());
      }
    });
    // Copy file from foo.txt to bar.txt synchronously
//    fs.copyBlocking("foo.txt", "bar.txt");

    // Read a file
    vertx.fileSystem().readFile("d:/foo.txt", result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
      } else {
        System.err.println("Oh oh ..." + result.cause());
      }
    });

    // Write a file
    vertx.fileSystem().writeFile("d:/foo.txt", Buffer.buffer("Hello"), result -> {
      if (result.succeeded()) {
        System.out.println("File written");
      } else {
        System.err.println("Oh oh ..." + result.cause());
      }
    });

    vertx.fileSystem().exists("d:/bar.txt", result -> {
      if (result.succeeded() && result.result()) {
        vertx.fileSystem().delete("d:/bar.txt", res -> {
          if (result.succeeded()) {
            System.out.println("File deleted");
          } else {
            System.err.println("Oh oh ..." + result.cause());
          }
        });
      }else {
        System.err.println("Oh oh ... - cannot delete the file: " + result.cause());
      }
    });
  }
}
