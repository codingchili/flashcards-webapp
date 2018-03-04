package com.codingchili.flashcards.handlers;

import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.*;
import com.codingchili.flashcards.request.HighscoreRequest;
import io.vertx.core.Future;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;
import com.codingchili.core.security.TokenFactory;

import static com.codingchili.core.protocol.RoleMap.*;

/**
 * handles the addition and retrieval of highscores.
 */
@Address("highscores")
public class HighscoreHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private TokenFactory tokenFactory = AppConfig.tokenFactory();
    private AsyncHighscoreStore highscores;
    private CoreContext core;

    @Override
    public void init(CoreContext core) {
        this.core = core;
    }

    @Override
    public void start(Future<Void> start) {
        HighscoreDB.create(core).setHandler(done -> {
            if (done.succeeded()) {
                this.highscores = done.result();
                start.complete();
            } else {
                start.fail(done.cause());
            }
        });
    }

    @Api(USER)
    public void add(HighscoreRequest request) {
        highscores.add(request.getCategoryId(), Highscore.of(request))
                .setHandler(request::result);
    }

    @Api(PUBLIC)
    public void list(HighscoreRequest request) {
        highscores.list(request.getCategoryId()).setHandler(request::result);
    }

    @Override
    public void handle(Request request) {
        protocol.get(request.route(), AppConfig.authorize(request))
                .submit(new HighscoreRequest(request));
    }
}
