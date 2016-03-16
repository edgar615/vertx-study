package com.edgar.vertx.http.simpleformupload;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;

/**
 * Created by Administrator on 2015/9/1.
 */
public class SimpleFormUploadServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SimpleFormUploadServer.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(request -> {
            if (request.uri().equals("/")) {
                request.response().sendFile("index.html");
            } else if (request.uri().startsWith("/form")) {
                request.setExpectMultipart(true);
                request.uploadHandler(upload -> {
                    upload.exceptionHandler(cause -> {
                        request.response().setChunked(true).end("upload failed");
                    });
                    upload.endHandler(v -> {
                        request.response().setChunked(true).end("Successfully uploaded to " + upload.filename());
                    });
                    // FIXME - Potential security exploit! In a real system you must check this filename
                    // to make sure you're not saving to a place where you don't want!
                    // Or better still, just use Vert.x-Web which controls the upload area.
                    upload.streamToFileSystem(upload.filename());
                });
            } else {
                request.response().setStatusCode(404).end();
            }
        }).listen(8080);
    }
}
