package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.CoreRuntimeException;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;
import com.codingchili.core.security.Token;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.AsyncCategoryStore;
import com.codingchili.flashcards.model.CategoryDB;
import com.codingchili.flashcards.model.FlashCategory;
import com.codingchili.flashcards.request.CategoryRequest;
import com.codingchili.flashcards.response.SizeResponse;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import static com.codingchili.flashcards.model.FlashCard.ID_CATEGORY;

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

    @Private("create")
    public void create(CategoryRequest request) {
        FlashCategory category = request.category();
        category.setOwner(request.sender());
        category.setCreated(ZonedDateTime.now());
        categories.save(category).setHandler(request::result);
    }

    @Private("authorize")
    public void authorize(CategoryRequest request) {
        categories.get(request.categoryName(), request.sender()).setHandler(query -> {
            if (query.succeeded()) {
                FlashCategory category = query.result();
                category.shareWith(request.users());
                categories.save(category).setHandler(request::result);
            } else {
                request.error(query.cause());
        }
    });
}

    @Public("public")
    public void publicCategories(CategoryRequest request) {
        categories.shared(request::result, request.categoryName());
    }

    @Private("share")
    public void share(CategoryRequest request) {
        categories.get(request.categoryName(), request.sender())
                .setHandler(query -> {
                    if (query.succeeded()) {
                        FlashCategory category = query.result();
                        if (category.getOwner().equals(request.sender())) {
                            category.setShared(true);
                            categories.save(category).setHandler(request::result);
                        } else {
                            request.error(new CoreRuntimeException("Not owner of category."));
                        }
                    } else {
                        request.error(query.cause());
                    }
                });
    }

    @Private("refresh")
    public void refresh(CategoryRequest request) {
        categories.available(request.sender()).setHandler(done -> {
            if (done.succeeded()) {
                Token token = request.token();
                token.addProperty(ID_CATEGORY, done.result().stream()
                        .map(FlashCategory::id)
                        .collect(Collectors.toList()));
                tokenFactory.sign(token);
                request.write(token);
            } else {
                request.write(done.cause());
            }
        });
    }

    @Private("list")
    public void list(CategoryRequest request) {
        categories.available(request.sender()).setHandler(request::result);
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
