package com.edgar.vertx.http;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;

/**
 * Created by Administrator on 2015/9/1.
 */
public class ClientExample {

  public static void main(String[] args) {
    //Creating an HTTP clien
    HttpClient client = Vertx.vertx().createHttpClient();

    HttpClientOptions options = new HttpClientOptions().setKeepAlive(false);
    client = Vertx.vertx().createHttpClient(options);

    //Making requests
    //Often you want to make many requests to the same host/port with an http client. To avoid
    // you repeating the host/port every time you make a request you can configure the client
    // with a default host/port:
    options = new HttpClientOptions().setDefaultHost("wibble.com");
    client = Vertx.vertx().createHttpClient(options);
    client.getNow("/some-uri", response -> System.out
            .println("Received response with status code " + response.statusCode()));

    //Alternatively if you find yourself making lots of requests to different host/ports with the
    // same client you can simply specify the host/port when doing the request.
    client = Vertx.vertx().createHttpClient();
    client.getNow(8080, "myserver.mycompany.com", "/some-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    });
    client.getNow("foo.othercompany.com", "/other-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    });

    //Simple requests with no request body
    // Send a GET request
    client.getNow("/some-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    });

// Send a GET request
    client.headNow("/other-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    });

    //Writing general requests
    client.request(HttpMethod.GET, "some-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    }).end();

    client.request(HttpMethod.POST, "foo-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    }).end("some-data");

    //Writing request bodies
    HttpClientRequest request = client.post("some-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    });
    // Now do stuff with the request
    request.putHeader("content-length", "1000");
    request.putHeader("content-type", "text/plain");
//        request.write(body);

// Make sure the request is ended when you're done with it
    request.end();

//        client.post("some-uri", response -> {
//            System.out.println("Received response with status code " + response.statusCode());
//        }).putHeader("content-length", "1000").putHeader("content-type", "text/plain").write
// (body).end();

//        client.post("some-uri", response -> {
//            System.out.println("Received response with status code " + response.statusCode());
//        }).putHeader("content-type", "text/plain").end(body);

    //Methods exist to write strings in UTF-8 encoding and in any specific encoding and to write
    // buffers:
    request.write("some data");

// Write string encoded in specific encoding
    request.write("some other data", "UTF-16");

// Write a buffer
    Buffer buffer = Buffer.buffer();
    buffer.appendInt(123).appendLong(245l);
    request.write(buffer);

    request.end("some simple data");

// Write buffer and end the request (send it) in a single call
    Buffer buffer2 = Buffer.buffer().appendDouble(12.34d).appendLong(432l);
    request.end(buffer2);

    //Writing request headers
    MultiMap headers = request.headers();
    headers.set("content-type", "application/json").set("other-header", "foo");

    //Chunked HTTP requests
    request.setChunked(true);
// Write some chunks
    for (int i = 0; i < 10; i++) {
      request.write("this-is-chunk-" + i);
    }
    request.end();

    //Request timeouts
//        request.setTimeout(1000)

    //Handling exceptions
    //XXXNow methods cannot receive an exception handler.
    request.exceptionHandler(e -> {
      System.out.println("Received exception: " + e.getMessage());
      e.printStackTrace();
    });
    //This does not handle non 2xx response that need to be handled in the HttpClientResponse code:
    request = client.post("some-uri", response -> {
      if (response.statusCode() == 200) {
        System.out.println("Everything fine");
        return;
      }
      if (response.statusCode() == 500) {
        System.out.println("Unexpected behavior on the server side");
        return;
      }
    });
    request.end();

    //Using the request as a stream
//        request.setChunked(true);
//        Pump pump = Pump.pump(file, request);
//        file.endHandler(v -> request.end());
//        pump.start();

    //Handling http responses
    client.getNow("some-uri", response -> {
      // the status code - e.g. 200 or 404
      System.out.println("Status code is " + response.statusCode());
      // the status message e.g. "OK" or "Not Found".
      System.out.println("Status message is " + response.statusMessage());
    });

    //Response headers and trailers
//        String contentType = response.headers().get("content-type");
//        String contentLength = response.headers().get("content-lengh");

    //Reading the request body
    client.getNow("some-uri", response -> {
      response.handler(buf -> {
        System.out.println("Received a part of the response body: " + buf);
      });
    });

    //If you know the response body is not very large and want to aggregate it all in memory
    // before handling it, you can either aggregate it yourself:
    client.getNow("some-uri", response -> {
      // Create an empty buffer
      Buffer totalBuffer = Buffer.buffer();
      response.handler(buf -> {
        System.out.println("Received a part of the response body: " + buf.length());
        totalBuffer.appendBuffer(buffer);
      });
      //The response endHandler is called when the entire response body has been read or
      // immediately after the headers have been read and the response handler has been called if
      // there is no body.
      response.endHandler(v -> {
        // Now all the body has been read
        System.out.println("Total response body length is " + totalBuffer.length());
      });
    });

    //Or you can use the convenience bodyHandler which is called with the entire body when the
    // response has been fully read:
    client.getNow("some-uri", response -> {
      response.bodyHandler(totalBuffer -> {
        // Now all the body has been read
        System.out.println("Total response body length is " + totalBuffer.length());
      });
    });

    //100-Continue handling
    HttpClientRequest request1 = client.put("some-uri", response -> {
      System.out.println("Received response with status code " + response.statusCode());
    });

    request1.putHeader("Expect", "100-Continue");

    request1.continueHandler(v -> {
      // OK to send rest of body
      request1.write("Some data");
      request1.write("Some more data");
      request1.end();
    });

    //Enabling compression on the client
    //Accept-Encoding: gzip, deflate
    //Content-Encoding: gzip
    //setTryUseCompression

    //Pooling and keep alive
    //setKeepAlive
    //When keep alive is enabled. Vert.x will add a Connection: Keep-Alive header to each HTTP
    // request sent.
    //The maximum number of connections to pool for each server is configured using setMaxPoolSize

    //Pipe-lining
    //To enable pipe-lining, it must be enabled using setPipelining. By default pipe-lining is
    // disabled.
    //When pipe-lining is enabled requests will be written to connections without waiting for
    // previous responses to return.
    //When pipe-line responses return at the client, the connection will be automatically closed
    // when all in-flight responses have returned and there are no outstanding pending requests
    // to write.

    //Server sharing
  }
}