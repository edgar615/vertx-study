package com.edgar.vertx.web.staticfile;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class StaticVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(StaticVerticle.class);
  }

  @Override
  public void start() throws Exception {

//    To serve static resources such as .html, .css, .js or any other static resource, you use an
// instance of StaticHandler.
//    Any requests to paths handled by the static handler will result in files being served from
// a directory on the file system or from the classpath. The default static file directory is
// webroot but this can be configured.
//            In the following example all requests to paths starting with /static/ will get
// served from the directory webroot:
    Router router = Router.router(vertx);
    ///static/css/mystyles.css => webroot/css/mystyles.css
    router.route("/static/*").handler(StaticHandler.create().setDirectoryListing(true));

//    By default the static handler will set cache headers to enable browsers to effectively
// cache files.
//    Vert.x-Web sets the headers cache-control,last-modified, and date.
//       cache-control is set to max-age=86400 by default. This corresponds to one day. This can
// be configured with setMaxAgeSeconds if required.
//    StaticHandler.create().setMaxAgeSeconds()


//    If a browser sends a GET or a HEAD request with an if-modified-since header and the
// resource has not been modified since that date, a 304 status is returned which tells the
// browser to use its locally cached resource.
//            If handling of cache headers is not required, it can be disabled with
// setCachingEnabled.
//    StaticHandler.create().setCachingEnabled(false)

//    Entries in the cache have an expiry time, and after that time, the file on disk will be
// checked again and the cache entry updated.
//
//    If you know that your files never change on disk, then the cache entry will effectively
// never expire. This is the default.
//
//    If you know that your files might change on disk when the server is running then you can
// set files read only to false with setFilesReadOnly.
//    StaticHandler.create().setFilesReadOnly(false)

//    To enable the maximum number of entries that can be cached in memory at any one time you
// can use setMaxCacheSize.
//
//            To configure the expiry time of cache entries you can use setCacheEntryTimeout.

//    Any requests to the root path / will cause the index page to be served. By default the
// index page is index.html. This can be configured with setIndexPage.

//    By default static resources will be served from the directory webroot. To configure this
// use setWebRoot.

//    By default the serve will serve hidden files (files starting with .). If you do not want
// hidden files to be served you can configure it with setIncludeHidden.

//    The server can also perform directory listing. By default directory listing is disabled. To
// enabled it use setDirectoryListing.

//    For text/html directory listing, the template used to render the directory listing page can
// be configured with setDirectoryTemplate.

//    By default, Vert.x will cache files that are served from the classpath into a file on disk
// in a sub-directory of a directory called .vertx in the current working directory. This is
// mainly useful when deploying services as fatjars in production where serving a file from the
// classpath every time can be slow.
    //To disable file caching you can provide the system property vertx.disableFileCaching with the value true.
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
