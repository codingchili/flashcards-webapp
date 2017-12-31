package com.codingchili.flashcards.handlers;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.logging.Level;
import com.codingchili.core.logging.Logger;
import com.codingchili.core.protocol.Address;
import com.codingchili.core.protocol.Api;
import com.codingchili.core.protocol.Protocol;
import com.codingchili.core.protocol.Roles;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.flashcards.AppConfig;
import com.codingchili.flashcards.model.AsyncCategoryStore;
import com.codingchili.flashcards.model.CategoryDB;
import com.codingchili.flashcards.model.FlashCategory;
import com.codingchili.flashcards.model.SimpleDate;
import com.codingchili.flashcards.request.CategoryRequest;
import com.codingchili.flashcards.response.SizeResponse;

import java.util.function.Consumer;

import static com.codingchili.core.protocol.RoleMap.PUBLIC;
import static com.codingchili.core.protocol.RoleMap.USER;

/**
 * Handler controller for categories.
 */
@Roles(USER)
@Address("categories")
public class CategoryHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private AsyncCategoryStore categories;
    private Logger logger;

    @Override
    public void init(CoreContext core) {
        this.categories = new CategoryDB(core);
        this.logger = core.logger(getClass());
    }

    @Api
    public void save(CategoryRequest request) {
        FlashCategory category = request.category();

        Consumer<FlashCategory> save = (saving) -> {
            saving.setOwner(request.sender());
            saving.setCreated(new SimpleDate());
            categories.save(saving).setHandler(request::result);
        };

        // if its an update to an existing: read existing values for rating.
        if (category.getId() != null) {
            categories.get(category.getId()).setHandler(done -> {
                if (done.succeeded()) {
                    FlashCategory old = done.result();
                    category.setRating(old.getRating());
                    category.setRateCount(old.getRateCount());
                    save.accept(category);
                } else {
                    save.accept(category);
                }
            });
        } else {
            // if its a new request: reset rating and rate count.
            category.setRating(0f);
            category.setRateCount(0);
            save.accept(category);
        }
    }

    @Api
    public void remove(CategoryRequest request) {
        categories.remove(request.sender(), request.categoryId())
                .setHandler(request::result);
    }

    @Api
    public void get(CategoryRequest request) {
        categories.get(request.categoryId()).setHandler(request::result);
    }

    @Api
    public void list(CategoryRequest request) {
        categories.list(request.sender()).setHandler(request::result);
    }

    @Api
    public void rate(CategoryRequest request) {
        categories.rate(
                request.categoryId(),
                request.sender(),
                request.rating()
        ).setHandler(request::result);
    }

    @Api
    public void search(CategoryRequest request) {
        long start = System.currentTimeMillis();
        categories.search(request.sender(), request.categoryName()).setHandler(done -> {
            request.result(done);
            logger.event("onSearchCompleted", Level.INFO)
                    .put("searchtime", (System.currentTimeMillis() - start) + "ms").send();
        });
    }

    @Api(value = PUBLIC, route = "search")
    public void shared(CategoryRequest request) {
        categories.search(request.categoryName()).setHandler(request::result);
    }

    @Api(PUBLIC)
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
        protocol.get(request.route(), AppConfig.authorize(request))
                .submit(new CategoryRequest(request));
    }
}
