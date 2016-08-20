package com.edgar.vertx.oauth2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.web.Router;

/**
 * Created by edgar on 16-8-20.
 */
public class ClientCredentialsFlow extends AbstractVerticle {
    public static void main(String[] args) {
        new Launcher().execute("run", ClientCredentialsFlow.class.getName());
    }

    @Override
    public void start() throws Exception {

        //OAuth2.0
        OAuth2ClientOptions credentials = new OAuth2ClientOptions()
                .setClientID("xxxxxxxxxxxxxxxxxxxx")
                .setClientSecret("XXXXXXXXXXXXXXXXXXXXX")
                .setSite("http://api.oauth.com");

        // Initialize the OAuth2 Library
        OAuth2Auth oauth2 = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, credentials);

        JsonObject tokenConfig = new JsonObject();

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/login").handler(rc -> {
            // Callbacks
// Save the access token
            oauth2.getToken(tokenConfig, res -> {
                if (res.failed()) {
                    System.err.println("Access Token Error: " + res.cause().getMessage());
                } else {
                    // Get the access token object (the authorization code is given from the previous step).
                    AccessToken token = res.result();

                    oauth2.api(HttpMethod.GET, "/users", new JsonObject().put("access_token", token.principal().getString("access_token")), res2 -> {
                        // the user object should be returned here...
                        System.out.println(res2.result());
                    });
                }
            });
        });

        server.requestHandler(router::accept).listen(8080);

    }

}
