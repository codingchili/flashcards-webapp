package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.Address;
import com.codingchili.core.protocol.Api;
import com.codingchili.core.protocol.Protocol;
import com.codingchili.core.protocol.Roles;
import com.codingchili.core.security.Account;
import com.codingchili.flashcards.model.AccountDB;
import com.codingchili.flashcards.model.AsyncAccountStore;
import com.codingchili.flashcards.request.AuthenticationRequest;
import com.codingchili.flashcards.response.SizeResponse;

import static com.codingchili.core.protocol.RoleMap.PUBLIC;
import static com.codingchili.core.protocol.RoleMap.USER;

/**
 * Handler controller for authentication.
 */
@Roles(PUBLIC)
@Address("accounts")
public class AuthenticationHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private AsyncAccountStore accounts;

    @Override
    public void init(CoreContext core) {
        this.accounts = new AccountDB(core);
    }

    @Api
    public void authenticate(AuthenticationRequest request) {
        accounts.authenticate(request.username(), request.password())
                .setHandler(request::result);
    }

    @Api
    public void register(AuthenticationRequest request) {
        Account account = new Account()
                .setPassword(request.password())
                .setUsername(request.username());

        accounts.register(account).setHandler(request::result);
    }

    @Api(USER)
    public void search(AuthenticationRequest request) {
        accounts.search(request.username()).setHandler(request::result);
    }

    @Api
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
