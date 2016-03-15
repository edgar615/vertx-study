package com.edgar.vertx.http;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;

/**
 * Created by Administrator on 2015/9/1.
 */
public class ServerExample {

    public static void main(String[] args) {
        //Creating an HTTP Server
        HttpServer server = Vertx.vertx().createHttpServer();

        //Configuring an HTTP server
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(100000);
        server = Vertx.vertx().createHttpServer(options);

        //Start the Server Listening
        //server.listen();
        //Or to specify the host and port in the call to listen, ignoring what is configured in the options:
        server.listen(8080, "myhost.com");
        //The default host is 0.0.0.0 which means 'listen on all available addresses' and the default port is 80.

        //The actual bind is asynchronous so the server might not actually be listening until some time after the call to listen has returned.
        //If you want to be notified when the server is actually listening you can provide a handler to the listen call. For example:
        server = Vertx.vertx().createHttpServer();
        server.listen(8080, "myhost.com", res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening!");
            } else {
                System.out.println("Failed to bind!");
            }
        });

        //Getting notified of incoming requests
        server.requestHandler(request -> {

        });

        //Handling requests
        Vertx.vertx().createHttpServer().requestHandler(request -> {
            //Reading Data from the Request Body
            request.handler(buffer -> {
                System.out.println("I have received a chunk of the body of length " + buffer.length());
            });

            request.response().end("hello world!");
        }).listen(8080);


        //The object passed into the handler is a Buffer, and the handler can be called multiple times as data arrives from the network, depending on the size of the body.
        //In some cases (e.g. if the body is small) you will want to aggregate the entire body in memory, so you could do the aggregation yourself as follows:

//        Buffer totalBuffer = Buffer.buffer();
//
//        request.handler(buffer -> {
//            System.out.println("I have received a chunk of the body of length " + buffer.length());
//            totalBuffer.appendBuffer(buffer);
//        });
//
//        request.endHandler(v -> {
//            System.out.println("Full body received, length = " + totalBuffer.length());
//        });

        //This is such a common case, that Vert.x provides a bodyHandler to do this for you. The body handler is called once when all the body has been received:
//        request.bodyHandler(totalBuffer -> {
//            System.out.println("Full body received, length = " + totalBuffer.length());
//        });

        //Handling HTML forms
        //If you want to retrieve the attributes of a multi-part form you should tell Vert.x that you expect to receive such a form before any of the body is read by calling setExpectMultipart with true, and then you should retrieve the actual attributes using formAttributes once the entire body has been read:
        server.requestHandler(request -> {
            request.setExpectMultipart(true);
            request.endHandler(v -> {
                // The body has now been fully read, so retrieve the form attributes
                MultiMap formAttributes = request.formAttributes();
            });
        });

        //Handling form file uploads
        server.requestHandler(request -> {
            request.setExpectMultipart(true);
            request.uploadHandler(upload -> {
                System.out.println("Got a file upload " + upload.name());
            });
        });
        //File uploads can be large we don’t provide the entire upload in a single buffer as that might result in memory exhaustion, instead, the upload data is received in chunks:
//        request.uploadHandler(upload -> {
//            upload.handler(chunk -> {
//                System.out.println("Received a chunk of the upload of length " + chunk.length());
//            });
//        });

        //If you just want to upload the file to disk somewhere you can use streamToFileSystem:
//        request.uploadHandler(upload -> {
//            upload.streamToFileSystem("myuploads_directory/" + upload.filename());
//        });

        //Sending back responses
        //Setting status code and message
        //setStatusCode
        //setStatusMessage

        //Writing HTTP responses
//        HttpServerResponse response = request.response();
//        response.write(buffer);

//        HttpServerResponse response = request.response();
//        response.write("hello world!");

//        HttpServerResponse response = request.response();
//        response.write("hello world!", "UTF-16");

        //Ending HTTP responses
//        HttpServerResponse response = request.response();
//        response.write("hello world!");
//        response.end();

//        HttpServerResponse response = request.response();
//        response.end("hello world!");

        //Closing the underlying connection
        //Non keep-alive connections will be automatically closed by Vert.x when the response is ended.
        //Keep-alive connections are not automatically closed by Vert.x by default. If you want keep-alive connections to be closed after an idle time, then you configure setIdleTimeout

        //Setting response headers
//        HttpServerResponse response = request.response();
//        MultiMap headers = response.headers();
//        headers.set("content-type", "text/html");
//        headers.set("other-header", "wibble");

//        HttpServerResponse response = request.response();
//        response.putHeader("content-type", "text/html").putHeader("other-header", "wibble");

        //Chunked HTTP responses and trailers
//        HttpServerResponse response = request.response();
//        response.setChunked(true);

        //Default is non-chunked. When in chunked mode, each call to one of the write methods will result in a new HTTP chunk being written out.
        //When in chunked mode you can also write HTTP response trailers to the response. These are actually written in the final chunk of the response.
//        HttpServerResponse response = request.response();
//        response.setChunked(true);
//        MultiMap trailers = response.trailers();
//        trailers.set("X-wibble", "woobble").set("X-quux", "flooble");

//        HttpServerResponse response = request.response();
//        response.setChunked(true);
//        response.putTrailer("X-wibble", "woobble").putTrailer("X-quux", "flooble");

        //Serving files directly from disk
        //If you were writing a web server, one way to serve a file from disk would be to open it as an AsyncFile and pump it to the HTTP response.
        //Or you could load it it one go using readFile and write it straight to the response.
        //Alternatively, Vert.x provides a method which allows you to serve a file from disk to an HTTP response in one operation. Where supported by the underlying operating system this may result in the OS directly transferring bytes from the file to the socket without being copied through user-space at all.
        //This is done by using sendFile, and is usually more efficient for large files, but may be slower for small files.

        Vertx.vertx().createHttpServer().requestHandler(request -> {
           String file = "";
            if (request.path().equals("/")) {
                file = "index.html";
            } else if (!request.path().contains("..")) {
                file = request.path();
            }
            request.response().sendFile("web/" + file);
        }).listen(8080);

        //Pumping responses
        //The server response is a WriteStream instance so you can pump to it from any ReadStream, e.g. AsyncFile, NetSocket, WebSocket or HttpServerRequest.
        Vertx.vertx().createHttpServer().requestHandler(request -> {
            HttpServerResponse response = request.response();
            if (request.method() == HttpMethod.PUT) {
                response.setChunked(true);
                Pump.pump(request, response).start();
                request.endHandler(v -> response.end());
            } else {
                response.setStatusCode(400).end();
            }
        }).listen(8080);

        //HTTP Compression
        //setCompressionSupported
    }
}
