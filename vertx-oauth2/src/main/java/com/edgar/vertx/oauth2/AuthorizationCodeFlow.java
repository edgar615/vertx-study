package com.edgar.vertx.oauth2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * Created by edgar on 16-8-20.
 */
public class AuthorizationCodeFlow extends AbstractVerticle {
    public static void main(String[] args) {
        new Launcher().execute("run", AuthorizationCodeFlow.class.getName());
    }

    @Override
    public void start() throws Exception {

        //OAuth2.0
        OAuth2ClientOptions credentials = new OAuth2ClientOptions()
                .setClientID("xxxxxxxxxxxxxxxxx")
                .setClientSecret("xxxxxxxxxxxxxxx")
                .setSite("https://github.com/login")
                .setTokenPath("/oauth/access_token")
                .setAuthorizationPath("/oauth/authorize");
//                .setSite("https://api.oauth.com");

        // Initialize the OAuth2 Library
        OAuth2Auth oauth2 = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, credentials);

        // Authorization oauth2 URI
        String authorization_uri = oauth2.authorizeURL(new JsonObject()
                .put("redirect_uri", "http://localhost:8080/callback")
                .put("scope", "notifications public_repo user")//多个scope用空格分隔
                .put("state", "unguessable"));//An unguessable random string.

        System.out.println(authorization_uri);

        TemplateEngine engine = HandlebarsTemplateEngine.create();

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/login").handler(rc -> {
            rc.put("name", "开放平台测试");

            engine.render(rc, "templates/index.hbs", res -> {
                if (res.succeeded()) {
                    rc.response().end(res.result());
                } else {
                    rc.fail(res.cause());
                }
            });
        });

        router.get("/github/login").handler(rc -> {
            // Redirect example using Vert.x
            rc.response().putHeader("Location", authorization_uri)
                    .setStatusCode(302)
                    .end();
        });

        router.get("/callback").handler(rc -> {
            //OAuth2.0
            String code = rc.request().getParam("code");
            System.out.println(code);
            JsonObject tokenConfig = new JsonObject()
                    .put("code", code)
                    .put("redirect_uri", "http://localhost:8080/callback");

            // Callbacks
            // Save the access token
            oauth2.getToken(tokenConfig, res -> {
                if (res.failed()) {
                    System.err.println("Access Token Error: " + res.cause().getMessage());
                } else {
                    // Get the access token object (the authorization code is given from the previous step).
                    AccessToken token = res.result();
                    System.out.println(token.principal());
                    rc.response().end(token.principal().encodePrettily());

                    // now check for permissions
                    token.isAuthorised("account:manage-account", r -> {
                        if (r.result()) {
                            // this user is authorized to manage its account
                            System.out.println("account:manage-account:" + r.result());
                        } else {
                            System.out.println("account:manage-account:failed" + r.cause());
                        }
                    });

                    token.isAuthorised("notifications", r -> {
                        if (r.result()) {
                            System.out.println("notifications:" + r.result());
                        } else {
                            System.out.println("notifications:failed" + r.cause());
                        }
                    });

                    //刷新
//                    if (token.expired()) {
//                        // Callbacks
//                        token.refresh(res -> {
//                            if (res.succeeded()) {
//                                // success
//                            } else {
//                                // error handling...
//                            }
//                        });
//                    }

                    //logout
//                    token.revoke("access_token", res -> {
//                        // Session ended. But the refresh_token is still valid.
//
//                        // Revoke the refresh_token
//                        token.revoke("refresh_token", res1 -> {
//                            System.out.println("token revoked.");
//                        });
//                    });
                }
            });
        });
        server.requestHandler(router::accept).listen(8080);

    }

}
