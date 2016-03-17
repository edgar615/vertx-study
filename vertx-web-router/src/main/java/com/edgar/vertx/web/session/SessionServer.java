package com.edgar.vertx.web.session;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

/**
 * Created by Administrator on 2015/9/7.
 */
public class SessionServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SessionServer.class);
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

//      Vert.x-Web uses session cookies to identify a session. The session cookie is temporary
// and will be deleted by your browser when it’s closed.
//
//      We don’t put the actual data of your session in the session cookie - the cookie simply
// uses an identifier to look-up the actual session on the server. The identifier is a random
// UUID generated using a secure random, so it should be effectively unguessable.
//
//              Cookies are passed across the wire in HTTP requests and responses so it’s always
// wise to make sure you are using HTTPS when sessions are being used. Vert.x will warn you if
// you attempt to use sessions over straight HTTP.
//
//      To enable sessions in your application you must have a SessionHandler on a matching route
// before your application logic.
//
//              The session handler handles the creation of session cookies and the lookup of the
// session so you don’t have to do that yourself.

      router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        router.route().handler(rc -> {
            Session session = rc.session();

            Integer cnt = session.get("hitcount");
            cnt = (cnt == null ? 0 : cnt) + 1;
            session.put("hitcount", cnt);
            rc.response().putHeader("content-type", "text/html")
                    .end("<html><body><h1>Hitcount: " + cnt + "</h1></body></html>");
        });

      //Local session store
//      SessionStore store1 = LocalSessionStore.create(vertx);
//
//// Create a local session store specifying the local shared map name to use
//// This might be useful if you have more than one application in the same
//// Vert.x instance and want to use different maps for different applications
//      SessionStore store2 = LocalSessionStore.create(vertx, "myapp3.sessionmap");
//
//// Create a local session store specifying the local shared map name to use and
//// setting the reaper interval for expired sessions to 10 seconds
//      SessionStore store3 = LocalSessionStore.create(vertx, "myapp3.sessionmap", 10000);

      //Clustered session store
//      Vertx.clusteredVertx(new VertxOptions().setClustered(true), res -> {
//
//        Vertx vertx = res.result();
//
//        // Create a clustered session store using defaults
//        SessionStore store1 = ClusteredSessionStore.create(vertx);
//
//        // Create a clustered session store specifying the distributed map name to use
//        // This might be useful if you have more than one application in the cluster
//        // and want to use different maps for different applications
//        SessionStore store2 = ClusteredSessionStore.create(vertx, "myclusteredapp3.sessionmap");
//      });
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
