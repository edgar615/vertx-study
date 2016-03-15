package com.edgar.vertx.http.simple;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2016/3/15.
 *
 * @author Edgar  Date 2016/3/15
 */
public class ReuquestServerExample extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(com.edgar.vertx.http.simple.ReuquestServerExample.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(request -> {
      JsonObject jsonObject = new JsonObject();
      jsonObject.put("method", request.method());
      jsonObject.put("version", request.version());
      jsonObject.put("uri", request.uri());
      jsonObject.put("path", request.path());
      jsonObject.put("query", request.query());
      jsonObject.put("remoteAddress", request.remoteAddress().toString());
      jsonObject.put("absoluteURI", request.absoluteURI());
      MultiMap headers = request.headers();
      jsonObject.put("user-agent", headers.get("user-agent"));
      jsonObject.put("headers", headers.toString());
      jsonObject.put("params", request.params().toString());

      //This allows the HTTP response body to be written in chunks, and is normally used when a
      // large response body is being streamed to a client and the total size is not known in advance.
//      response.setChunked(true);

//      When in chunked mode you can also write HTTP response trailers to the response. These are actually written in the final chunk of the response
//      response.setChunked(true);
//      response.putTrailer("X-wibble", "woobble").putTrailer("X-quux", "flooble");

      request.response().setChunked(true).putHeader("content-type", "text/html").putHeader
              ("other-header",
                                                                           "wibble").putTrailer("X-wibble", "woobble").putTrailer("X-quux", "flooble").end(jsonObject.encode());
      Buffer totalBuffer = Buffer.buffer();
      request.handler(buffer -> {
        System.out.println("I have received a chunk of the body of length " + buffer.length());
        totalBuffer.appendBuffer(buffer);
      });
      request.endHandler(
              v -> System.out.println("Full body received, length = " + totalBuffer.length()));

//      This is such a common case, that Vert.x provides a bodyHandler to do this for you. The
// body handler is called once when all the body has been received:
//      request.bodyHandler(totalBuffer -> {
//        System.out.println("Full body received, length = " + totalBuffer.length());
//      });

    }).listen(8080, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server is now listening!");
      } else {
        System.out.println("Failed to bind!");
      }
    });
  }
}
