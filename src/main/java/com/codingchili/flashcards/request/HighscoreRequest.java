package com.codingchili.flashcards.request;

import com.codingchili.core.listener.Request;
import com.codingchili.core.listener.RequestWrapper;

import static com.codingchili.flashcards.model.FlashCard.ID_CATEGORY;

/**
 * Request object for the highscore handler.
 */
public class HighscoreRequest extends RequestWrapper {
    private static final String CORRECT = "correct";
    private static final String WRONG = "wrong";
    private static final String SECONDS = "seconds";
    private static final String CARDS = "cards";

    public HighscoreRequest(Request request) {
        super(request);
    }

    public String getCategoryId() {
        return data().getString("id");
    }

    public String getUserId() {
        return token().getDomain();
    }

    public Integer getCorrect() {
        return data().getInteger(CORRECT);
    }

    public Integer getWrong() {
        return data().getInteger(WRONG);
    }

    public int getSeconds() {
        return data().getInteger(SECONDS);
    }

    public int getCards() {
        return data().getInteger(CARDS);
    }
}
