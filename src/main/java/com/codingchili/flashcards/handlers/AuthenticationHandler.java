package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;
import com.codingchili.core.security.Account;
import com.codingchili.flashcards.model.AccountDB;
import com.codingchili.flashcards.model.AsyncAccountStore;
import com.codingchili.flashcards.request.AuthenticationRequest;
import com.codingchili.flashcards.response.SizeResponse;

/**
 * Handler controller for authentication.
 */
@Address("login")
public class AuthenticationHandler implements CoreHandler {
    private Protocol<RequestHandler<Request>> protocol = new Protocol<>(this);
    private AsyncAccountStore accounts;

    @Override
    public void init(CoreContext core) {
        this.accounts = new AccountDB(core);
    }

    @Public("authenticate")
    public void authenticate(AuthenticationRequest request) {
        accounts.authenticate(request.username(), request.password())
                .setHandler(request::result);
    }

    @Public("register")
    public void register(AuthenticationRequest request) {
        Account account = new Account()
                .setPassword(request.password())
                .setUsername(request.username());

        accounts.register(account).setHandler(request::result);
    }

    @Private("search")
    public void search(AuthenticationRequest request) {
        accounts.search(request.username()).setHandler(request::result);
    }

    @Public("size")
    public void size(AuthenticationRequest request) {
        accounts.size().setHandler(count -> {
            if (count.succeeded()) {
                request.write(new SizeResponse(count.result()));
            } else {
                request.error(count.cause());
            }
        });
    }

    @Override
    public void handle(Request request) {
        protocol.get(request.route()).handle(new AuthenticationRequest(request));
    }
}
