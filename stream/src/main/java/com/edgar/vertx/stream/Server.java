package com.edgar.vertx.stream;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.Pump;

/**
 * Created by Administrator on 2015/9/1.
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetServer().connectHandler(socket -> {
      Pump.pump(socket, socket).start();
    }).listen(1234);

    System.out.println("Echo server is now listening");


//    ReadStream
//
//    ReadStream is implemented by HttpClientResponse, DatagramSocket, HttpClientRequest,
// HttpServerFileUpload, HttpServerRequest, HttpServerRequestStream, MessageConsumer, NetSocket,
// NetSocketStream, WebSocket, WebSocketStream, TimeoutStream, AsyncFile.
//
//            Functions:
//
//    handler: set a handler which will receive items from the ReadStream.
//
//    pause: pause the handler. When paused no items will be received in the handler.
//
//    resume: resume the handler. The handler will be called if any item arrives.
//
//            exceptionHandler: Will be called if an exception occurs on the ReadStream.
//
//    endHandler: Will be called when end of stream is reached. This might be when EOF is reached
// if the ReadStream represents a file, or when end of request is reached if it’s an HTTP
// request, or when the connection is closed if it’s a TCP socket.
//


//    WriteStream
//
//    WriteStream is implemented by HttpClientRequest, HttpServerResponse WebSocket, NetSocket,
// AsyncFile, PacketWritestream and MessageProducer
//
//    Functions:
//
//    write: write an object to the WriteStream. This method will never block. Writes are queued
// internally and asynchronously written to the underlying resource.
//
//            setWriteQueueMaxSize: set the number of object at which the write queue is
// considered full, and the method writeQueueFull returns true. Note that, when the write queue
// is considered full, if write is called the data will still be accepted and queued. The actual
// number depends on the stream implementation, for Buffer the size represents the actual number
// of bytes written and not the number of buffers.
//
//    writeQueueFull: returns true if the write queue is considered full.
//
//    exceptionHandler: Will be called if an exception occurs on the WriteStream.
//
//    drainHandler: The handler will be called if the WriteStream is considered no longer full.
//

//
//    Pump
//
//    Instances of Pump have the following methods:
//
//    start: Start the pump.
//
//            stop: Stops the pump. When the pump starts it is in stopped mode.
//
//            setWriteQueueMaxSize: This has the same meaning as setWriteQueueMaxSize on the
// WriteStream.
//
//    A pump can be started and stopped multiple times.
//
//            When a pump is first created it is not started. You need to call the start() method
// to start it.


  }
}