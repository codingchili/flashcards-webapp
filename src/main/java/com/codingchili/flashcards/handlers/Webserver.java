package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreService;
import com.codingchili.core.listener.ListenerSettings;
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

        router.route("/*").handler(StaticHandler.create()
                .setCachingEnabled(false)
                .setWebRoot(POLYMER));

        core.vertx().createHttpServer(new ListenerSettings().getHttpOptions(core))
                .requestHandler(router::accept)
                .listen(443, start);
    }
}
