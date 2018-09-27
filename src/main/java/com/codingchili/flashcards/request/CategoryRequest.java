package com.codingchili.flashcards.request;

import com.codingchili.core.listener.Request;
import com.codingchili.core.listener.RequestWrapper;
import com.codingchili.core.protocol.Serializer;
import com.codingchili.flashcards.model.FlashCategory;

import java.util.List;

import static com.codingchili.flashcards.model.FlashCard.ID_CATEGORY;
import static com.codingchili.flashcards.model.FlashCategory.ID_USERS;

/**
 * Helper class to interact with the request data.
 */
public class CategoryRequest implements RequestWrapper {
    private Request request;

    public CategoryRequest(Request request) {
        this.request = request;
    }

    public String categoryName() {
        return data().getString(ID_CATEGORY);
    }

    public String categoryId() {
        return data().getString("id");
    }

    public FlashCategory category() {
        return Serializer.unpack(data(), FlashCategory.class);
    }

    public String sender() {
        return token().getDomain();
    }

    public List<String> users() {
        return data().getJsonArray(ID_USERS).getList();
    }

    public Integer rating() {
        return data().getInteger("rating");
    }

    @Override
    public Request request() {
        return request;
    }
}
