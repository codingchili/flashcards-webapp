package com.codingchili.flashcards;

import com.codingchili.core.Launcher;
import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.LaunchContext;
import com.codingchili.core.files.Configurations;
import com.codingchili.core.listener.BusRouter;
import com.codingchili.core.listener.CoreService;
import com.codingchili.core.listener.ListenerSettings;
import com.codingchili.core.listener.transport.RestListener;
import com.codingchili.flashcards.handlers.AuthenticationHandler;
import com.codingchili.flashcards.handlers.CardHandler;
import com.codingchili.flashcards.handlers.CategoryHandler;
import com.codingchili.flashcards.handlers.Webserver;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * Application launcher, entry point.
 */
public class Service implements CoreService {
    private static final String FLASHCARDS = "flashcards";
    private CoreContext core;

    public static void main(String[] args) {
        Configurations.system()
                .setHandlers(1)
                .setListeners(1);

        Configurations.launcher()
                .deployable(Service.class)
                .setClustered(false);

        Launcher.start(new LaunchContext(args));
    }

    @Override
    public void init(CoreContext core) {
        this.core = core;
    }

    public void start(Future start) {
        CompositeFuture.all(
                core.handler(AuthenticationHandler::new),
                core.handler(CardHandler::new),
                core.handler(CategoryHandler::new),
                core.service(Webserver::new),
                core.listener(() -> new RestListener()
                        .settings(() -> new ListenerSettings()
                                .setMaxRequestBytes(512))
                        .handler(new BusRouter()))
        ).setHandler(start);
    }
}