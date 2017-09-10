package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;
import com.codingchili.core.protocol.exception.AuthorizationRequiredException;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.AsyncCategoryStore;
import com.codingchili.flashcards.model.CategoryDB;
import com.codingchili.flashcards.model.FlashCategory;
import com.codingchili.flashcards.request.CategoryRequest;
import com.codingchili.flashcards.response.SizeResponse;

import java.time.ZonedDateTime;

import static com.codingchili.core.configuration.CoreStrings.ANY;

/**
 * Handler controller for categories.
 */
@Address("categories")
public class CategoryHandler implements CoreHandler {
    private Protocol<RequestHandler<Request>> protocol = new Protocol<>(this);
    private TokenFactory tokenFactory = AppConfig.factory();
    private AsyncCategoryStore categories;

    @Override
    public void init(CoreContext core) {
        this.categories = new CategoryDB(core);
    }

    @Private("save")
    public void save(CategoryRequest request) {
        FlashCategory category = request.category();
        category.setOwner(request.sender());
        category.setCreated(ZonedDateTime.now());
        categories.save(category).setHandler(request::result);
    }

    @Private("list")
    public void list(CategoryRequest request) {
        categories.list(request.sender()).setHandler(request::result);
    }

    @Private("search")
    public void search(CategoryRequest request) {
        categories.search(request.categoryName(), request.sender()).setHandler(request::result);
    }

    @Public("search")
    public void shared(CategoryRequest request) {
        categories.search(request.categoryName()).setHandler(request::result);
    }

    @Public("size")
    public void size(CategoryRequest request) {
        categories.size().setHandler(count -> {
            if (count.succeeded()) {
                request.write(new SizeResponse(count.result()));
            } else {
                request.error(count.cause());
            }
        });
    }

    @Override
    public void handle(Request request) {
        protocol.get(access(request), request.route())
                .handle(new CategoryRequest(request));
    }

    private Access access(Request request) {
        if (tokenFactory.verifyToken(request.token())) {
            return Access.AUTHORIZED;
        } else {
            return Access.PUBLIC;
        }
    }
}
