package com.codingchili.flashcards.request;

import com.codingchili.core.listener.Request;
import com.codingchili.core.listener.RequestWrapper;
import com.codingchili.core.protocol.Serializer;
import com.codingchili.flashcards.model.FlashCard;

/**
 *
 */
public class CardRequest extends RequestWrapper {

    public CardRequest(Request request) {
        super(request);
    }

    public FlashCard getCard() {
        return Serializer.unpack(data(), FlashCard.class);
    }
}
