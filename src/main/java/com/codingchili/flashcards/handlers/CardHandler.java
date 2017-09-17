package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;
import com.codingchili.core.protocol.exception.AuthorizationRequiredException;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.*;
import com.codingchili.flashcards.request.CardRequest;
import com.codingchili.flashcards.response.SizeResponse;

import static com.codingchili.core.protocol.Access.AUTHORIZED;
import static com.codingchili.core.protocol.Access.PUBLIC;

/**
 * Handler controller for cards.
 */
@Address("cards")
public class CardHandler implements CoreHandler {
    private Protocol<RequestHandler<Request>> protocol = new Protocol<>(this);
    private TokenFactory factory = AppConfig.factory();
    private AsyncCardStore cards;
    private AsyncCategoryStore categories;

    @Override
    public void init(CoreContext core) {
        this.cards = new CardDB(core);
        this.categories = new CategoryDB(core);
    }

    @Private("create")
    public void create(CardRequest request) {
        if (request.categoryOwned()) {
            cards.add(request.card().setOwner(request.token().getDomain()))
                    .setHandler(request::result);
        } else {
            request.error(new AuthorizationRequiredException());
        }
    }

    @Private("remove")
    public void remove(CardRequest request) {
        if (request.categoryOwned()) {
            cards.remove(request.card().getId()).setHandler(request::result);
        } else {
            request.error(new AuthorizationRequiredException());
        }
    }

    @Private("list")
    public void list(CardRequest request) {
        if (request.categorySharedWith() || request.categoryOwned()) {
            cards.get(request.sender(), request.category()).setHandler(request::result);
        } else {
            request.error(new AuthorizationRequiredException());
        }
    }

    @Public("size")
    public void size(CardRequest request) {
        cards.size().setHandler(count -> {
            request.write(new SizeResponse(count.result()));
        });
    }

    @Override
    public void handle(Request req) {
        CardRequest request = new CardRequest(req);

        if (factory.verifyToken(request.token())) {
            FlashCard card = request.card();
            categories.get(card.getCategory()).setHandler(get -> {
                request.setCategory(get.result());
                handle(AUTHORIZED, request);
            });
        } else {
            handle(PUBLIC, request);
        }
    }

    private void handle(Access access, CardRequest request) {
        protocol.get(access, request.route()).handle(request);
    }
}
