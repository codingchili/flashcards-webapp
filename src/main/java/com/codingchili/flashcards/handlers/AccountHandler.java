package com.codingchili.flashcards.handlers;

import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.*;
import com.codingchili.flashcards.request.AccountRequest;
import com.codingchili.flashcards.response.SizeResponse;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;

import static com.codingchili.core.protocol.RoleMap.*;

/**
 * Handler controller for authentication.
 */
@Roles(PUBLIC)
@Address("accounts")
public class AccountHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private AsyncAccountStore accounts;

    @Override
    public void init(CoreContext core) {
        this.accounts = new AccountDB(core);
    }

    @Api
    public void authenticate(AccountRequest request) {
        accounts.authenticate(request.username(), request.password())
                .setHandler(request::result);
    }

    @Api
    public void register(AccountRequest request) {
        FlashAccount account = new FlashAccount();
        account.setPassword(request.password());
        account.setUsername(request.username());
        accounts.register(account).setHandler(request::result);
    }

    @Api(USER)
    public void feedback(AccountRequest request) {
        AccountMessage message = new AccountMessage()
                .setTitle(request.title())
                .setBody(request.body())
                .setSender(request.authenticatedUser());

        accounts.message(request.receiver(), message).setHandler(request::result);
    }

    @Api(USER)
    public void inbox(AccountRequest request) {
        accounts.inbox(request.authenticatedUser()).setHandler(request::result);
    }

    @Api(USER)
    public void read(AccountRequest request) {
        accounts.read(request.authenticatedUser(), request.message()).setHandler(request::result);
    }

    @Api(USER)
    @Description("Updates the password of an existing user.")
    public void updatePassword(AccountRequest request) {
        accounts.updatePassword(
                request.authenticatedUser(),
                request.oldpassword(),
                request.password())
                    .setHandler(request::result);
    }

    @Api(USER)
    public void search(AccountRequest request) {
        accounts.search(request.username()).setHandler(request::result);
    }

    @Api
    public void size(AccountRequest request) {
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
        protocol.get(request.route(), AppConfig.authorize(request))
                .submit(new AccountRequest(request));
    }
}
