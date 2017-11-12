package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.Address;
import com.codingchili.core.protocol.Api;
import com.codingchili.core.protocol.Protocol;
import com.codingchili.core.protocol.Role;
import com.codingchili.core.protocol.exception.AuthorizationRequiredException;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.*;
import com.codingchili.flashcards.request.CardRequest;
import com.codingchili.flashcards.response.SizeResponse;

import static com.codingchili.core.protocol.RoleMap.PUBLIC;

/**
 * Handler controller for cards.
 */
@Address("cards")
public class CardHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private TokenFactory factory = AppConfig.tokenFactory();
    private AsyncCardStore cards;
    private AsyncCategoryStore categories;

    @Override
    public void init(CoreContext core) {
        this.cards = new CardDB(core);
        this.categories = new CategoryDB(core);
    }

    @Api
    public void create(CardRequest request) {
        if (request.categoryOwned()) {
            cards.add(request.card().setOwner(request.token().getDomain()))
                    .setHandler(request::result);
        } else {
            request.error(new AuthorizationRequiredException());
        }
    }

    @Api
    public void remove(CardRequest request) {
        if (request.categoryOwned()) {
            cards.remove(request.card().getId()).setHandler(request::result);
        } else {
            request.error(new AuthorizationRequiredException());
        }
    }

    @Api
    public void list(CardRequest request) {
        if (request.categorySharedWith() || request.categoryOwned() || request.category().isShared()) {
            cards.get(request.sender(), request.category()).setHandler(request::result);
        } else {
            request.error(new AuthorizationRequiredException());
        }
    }

    @Api(PUBLIC)
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
            if (card.getCategory() != null) {
                categories.get(card.getCategory()).setHandler(get -> {
                    request.setCategory(get.result());
                    handle(Role.USER, request);
                });
            } else {
                handle(Role.USER, request);
            }
        } else {
            handle(Role.PUBLIC, request);
        }
    }

    private void handle(Role role, CardRequest request) {
        protocol.get(request.route(), role).submit(request);
    }
}
