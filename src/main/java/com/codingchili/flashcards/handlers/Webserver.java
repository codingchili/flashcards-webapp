package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreService;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Webserver to serve the web interface.
 */
public class Webserver implements CoreService {
    private static final String POLYMER = "polymer/";
    private CoreContext core;

    public void init(CoreContext core) {
        this.core = core;
    }

    public void start(Future start) {
        Router router = Router.router(core.vertx());
        router.route().handler(BodyHandler.create());

        router.route("/*").handler(ctx -> {
            // let the clientside router handle views to not break links.
            if (ctx.request().path().contains(".")) {
                ctx.next();
            } else if (!ctx.request().path().equals("/")) {
                ctx.reroute("/");
            } else {
                ctx.next();
            }
        });

        router.route("/*").handler(StaticHandler.create()
                .setCachingEnabled(false)
                .setWebRoot(POLYMER));

        core.vertx().createHttpServer().requestHandler(router::accept)
                .listen(80, start);
    }

}
