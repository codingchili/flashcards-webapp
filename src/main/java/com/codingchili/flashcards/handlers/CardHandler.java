package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.CardDB;
import com.codingchili.flashcards.model.FlashCard;
import com.codingchili.flashcards.request.CardRequest;
import com.codingchili.flashcards.response.SizeResponse;

import java.util.Collection;

import static com.codingchili.flashcards.model.FlashCard.ID_CATEGORY;

/**
 * Handler controller for cards.
 */
@Address("cards")
public class CardHandler implements CoreHandler {
    private Protocol<RequestHandler<Request>> protocol = new Protocol<>(this);
    private TokenFactory factory = AppConfig.factory();
    private CardDB cards;

    @Override
    public void init(CoreContext core) {
        this.cards = new CardDB(core);
    }

    // todo ask category access on bus or check token?

    @Private("add")
    public void add(CardRequest request) {
        // todo check category in token properties
        cards.add(request.getCard().setOwner(request.token().getDomain()))
                .setHandler(request::result);
    }

    @Private("remove")
    public void remove(CardRequest request) {
        // todo check category in token properties
        cards.remove(request.getCard().getId())
                .setHandler(request::result);
    }

    @Private("get")
    public void cards(CardRequest request) {
        // todo check category in token properties
        // todo or if category is public
        cards.get(request.getCard().getCategory())
                .setHandler(request::result);
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
        protocol.get(access(request), request.route())
                .handle(request);
    }

    private Access access(CardRequest request) {
        if (factory.verifyToken(request.token())) {
            FlashCard card = request.getCard();
            Collection<String> categories = request.token().getProperty(ID_CATEGORY);

            // require the token to contain the requested category.
            if (categories != null && categories.contains(card.getCategory())) {
                return Access.AUTHORIZED;
            }
        }
        return Access.PUBLIC;
    }
}
