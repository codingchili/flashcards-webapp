package com.codingchili.flashcards;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.LaunchContext;
import com.codingchili.core.listener.BusRouter;
import com.codingchili.core.listener.CoreService;
import com.codingchili.core.listener.ListenerSettings;
import com.codingchili.core.listener.transport.RestListener;
import com.codingchili.flashcards.handlers.AccountHandler;
import com.codingchili.flashcards.handlers.CardHandler;
import com.codingchili.flashcards.handlers.CategoryHandler;
import com.codingchili.flashcards.handlers.HighscoreHandler;
import com.codingchili.flashcards.handlers.Webserver;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.Arrays;

import static com.codingchili.core.files.Configurations.*;

/**
 * Application launcher, entry point.
 */
public class Service implements CoreService {
    private CoreContext core;

    public static void main(String[] args) {
        system().setHandlers(1).setListeners(1).setServices(1).setMetrics(false);
        storage().setMaxResults(64);
        launcher().setVersion("1.0.8").setApplication("flashcards")
                .deployable(Service.class)
                .setClustered(false);

        new LaunchContext(args).start();
    }

    @Override
    public void init(CoreContext core) {
        this.core = core;
    }

    @Override
    public void start(Future start) {
        CompositeFuture.all(Arrays.asList(
                core.handler(AccountHandler::new),
                core.handler(CardHandler::new),
                core.handler(CategoryHandler::new),
                core.handler(HighscoreHandler::new),
                //core.handler(TransactionHandler::new),

                core.service(Webserver::new),

                core.listener(() -> new RestListener()
                        .settings(() -> new ListenerSettings()
                                .setPort(8180)
                                .setMaxRequestBytes(16384))
                        .handler(new BusRouter())))
        ).setHandler(start);
    }
}