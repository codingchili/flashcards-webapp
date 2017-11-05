package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.Address;
import com.codingchili.core.protocol.Api;
import com.codingchili.core.protocol.Protocol;
import com.codingchili.core.protocol.Role;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.AsyncHighscoreStore;
import com.codingchili.flashcards.model.Highscore;
import com.codingchili.flashcards.model.HighscoreDB;
import com.codingchili.flashcards.request.HighscoreRequest;
import io.vertx.core.Future;

import static com.codingchili.core.protocol.RoleMap.PUBLIC;
import static com.codingchili.core.protocol.RoleMap.USER;

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
        highscores.add(request.getCategory(), Highscore.of(request))
                .setHandler(request::result);
    }

    @Api(PUBLIC)
    public void list(HighscoreRequest request) {
        request.result(highscores.list(request.getCategory()));
    }

    @Override
    public void handle(Request request) {
        protocol.get(request.route(), access(request))
                .submit(new HighscoreRequest(request));
    }

    private Role access(Request request) {
        if (tokenFactory.verifyToken(request.token())) {
            return Role.USER;
        } else {
            return Role.PUBLIC;
        }
    }
}
