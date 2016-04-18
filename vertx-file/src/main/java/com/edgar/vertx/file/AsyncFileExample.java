package com.edgar.vertx.file;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.Pump;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class AsyncFileExample extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(AsyncFileExample.class);
  }

  @Override
  public void start() throws Exception {
    //AsyncFile implements ReadStream and WriteStream so you can pump files to and from other
    // stream objects such as net sockets, http requests and responses, and WebSockets.
    OpenOptions openOptions = new OpenOptions();
    vertx.fileSystem().open("d:/foo.txt", openOptions, res -> {
      if (res.succeeded()) {
        AsyncFile file = res.result();
        System.out.println(file);
      } else {
        System.err.println("Oh oh ..." + res.cause());
      }
    });

//    Random access writes
//
//    To use an AsyncFile for random access writing you use the write method.
//
//    The parameters to the method are:
//
//    buffer: the buffer to write.
//
//    position: an integer position in the file where to write the buffer. If the position is
// greater or equal to the size of the file, the file will be enlarged to accommodate the offset.
//
//    handler: the result handler
    vertx.fileSystem().open("d:/foo.txt", openOptions, res -> {
      if (res.succeeded()) {
        AsyncFile file = res.result();
        Buffer buff = Buffer.buffer("foo");
        for (int i = 0; i < 5; i++) {
          file.write(buff, buff.length() * i, ar -> {
            if (ar.succeeded()) {
              System.out.println("Written ok!");
              // etc
            } else {
              System.err.println("Failed to write: " + ar.cause());
            }
          });
        }
      } else {
        System.err.println("Oh oh ..." + res.cause());
      }
    });
//    Random access reads
//
//    To use an AsyncFile for random access reads you use the read method.
//
//    The parameters to the method are:
//
//    buffer: the buffer into which the data will be read.
//
//            offset: an integer offset into the buffer where the read data will be placed.
//
//            position: the position in the file where to read data from.
//
//    length: the number of bytes of data to read
//
//    handler: the result handler
//
    vertx.fileSystem().open("d:/foo.txt", new OpenOptions(), result -> {
      if (result.succeeded()) {
        AsyncFile file = result.result();
        Buffer buff = Buffer.buffer(1000);
        for (int i = 0; i < 10; i++) {
          file.read(buff, i * 100, i * 100, 100, ar -> {
            if (ar.succeeded()) {
              System.out.println("Read ok!" + ar.result());
            } else {
              System.err.println("Failed to write: " + ar.cause());
            }
          });
        }
      } else {
        System.err.println("Cannot open file " + result.cause());
      }
    });


//    When opening an AsyncFile, you pass an OpenOptions instance.These options describe the
//    behavior of the file access.For instance, you can configure the file permissions with
//    the setRead, setWrite and setPerms methods.
//
//            You can also configure the behavior if the open file already exists with
//    setCreateNew and setTruncateExisting.
//
//            You can also mark the file to be deleted on close or when the JVM is shutdown
//    with setDeleteOnClose.

//    Flushing data to underlying storage.
//
//            In the OpenOptions, you can enable/disable the automatic synchronisation of the
// content on every write using setDsync. In that case, you can manually flush any writes from
// the OS cache by calling the flush method.

    //Using AsyncFile as ReadStream and WriteStream
    final AsyncFile output = vertx.fileSystem().openBlocking("d:/plagiary.txt", new OpenOptions());

    vertx.fileSystem().open("d:/foo.txt", new OpenOptions(), result -> {
      if (result.succeeded()) {
        AsyncFile file = result.result();
        Pump.pump(file, output).start();
        file.endHandler((r) -> {
          System.out.println("Copy done");
        });
      } else {
        System.err.println("Cannot open file " + result.cause());
      }
    });


//    Accessing files from the classpath
//
//    When vert.x cannot find the file on the filesystem it tries to resolve the file from the
// class path. Note that classpath resource paths never start with a /.
//
//    Due to the fact that Java does not offer async access to classpath resources, the file is
// copied to the filesystem in a worker thread when the classpath resource is accessed the very
// first time and served from there asynchrously. When the same resource is accessed a second
// time, the file from the filesystem is served directly from the filesystem. The original
// content is served even if the classpath resource changes (e.g. in a development system).
//
//    This caching behaviour can be disabled by setting the system property vertx
// .disableFileCaching to true. The path where the files are cached is .vertx by default and can
// be customized by setting the system property vertx.cacheDirBase.
//
//            The whole classpath resolving feature can be disabled by setting the system
// property vertx.disableFileCPResolving to true.
//            Closing an AsyncFile
//
//    To close an AsyncFile call the close method. Closing is asynchronous and if you want to be notified when the close has been completed you can specify a handler function as an argument.

  }
}
