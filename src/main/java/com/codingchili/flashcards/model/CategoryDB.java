package com.codingchili.flashcards.model;

import com.codingchili.flashcards.AppConfig;
import io.vertx.core.Future;

import java.util.Collection;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.protocol.exception.AuthorizationRequiredException;
import com.codingchili.core.storage.*;

import static com.codingchili.flashcards.model.FlashCard.ID_OWNER;
import static com.codingchili.flashcards.model.FlashCategory.*;

/**
 * Handles storage of categories.
 */
public class CategoryDB implements AsyncCategoryStore {
    private static final String ARRAY = "[]";
    private VoterEventStore voters;
    private AsyncStorage<FlashCategory> categories;

    public CategoryDB(CoreContext core) {
        new StorageLoader<FlashCategory>(core)
                .withPlugin(AppConfig.storage())
                .withValue(FlashCategory.class)
                .withDB(AppConfig.db(), "categories")
                .build(done -> categories = done.result());

        VoterEventStore.create(core).setHandler(done -> {
            if (done.succeeded()) {
                voters = done.result();
            } else {
                categories.context().logger(getClass()).onError(done.cause());
            }
        });
    }

    @Override
    public Future<FlashCategory> get(String id) {
        Future<FlashCategory> future = Future.future();
        categories.get(id, future);
        return future;
    }

    @Override
    public Future<Void> save(FlashCategory category) {
        Future<Void> future = Future.future();
        categories.put(category, future);
        return future;
    }

    @Override
    public Future<Collection<FlashCategory>> list(String username) {
        Future<Collection<FlashCategory>> future = Future.future();
        categories.query(ID_OWNER).equalTo(username)
                .pageSize(256)
                .orderBy(FlashCategory.ID_INDEXED_NAME)
                .execute(future);
        return future;
    }

    @Override
    public Future<Collection<FlashCategory>> search(String username, String query) {
        query = query.toLowerCase();

        Future<Collection<FlashCategory>> future = Future.future();
        categories
                .query(ID_OWNER).equalTo(username).and(ID_INDEXED_NAME).like(query)
                .or(ID_USERS + ARRAY).equalTo(username).and(ID_INDEXED_NAME).like(query)
                .or(ID_SHARED).equalTo(true).and(ID_INDEXED_NAME).like(query)
                .or(ID_COST).between(1L, Long.MAX_VALUE).and(ID_INDEXED_NAME).like(query)
                .orderBy(FlashCategory.ID_RATING).order(SortOrder.DESCENDING)
                .execute(categories -> {
                    if (categories.succeeded()) {
                        future.complete(categories.result());
                    } else {
                        future.fail(categories.cause());
                    }
                });
        return future;
    }

    @Override
    public Future<Collection<FlashCategory>> search(String query) {
        Future<Collection<FlashCategory>> future = Future.future();
        categories.query(ID_SHARED).equalTo(true)
                .orderBy(FlashCategory.ID_RATING)
                .execute(future);
        return future;
    }

    @Override
    public Future<Integer> size() {
        Future<Integer> future = Future.future();
        categories.size(future);
        return future;
    }

    @Override
    public Future<Void> remove(String username, String categoryId) {
        Future<Void> future = Future.future();
        categories.get(categoryId, get -> {
            FlashCategory category = get.result();

            if (get.succeeded()) {
                if (category.getOwner().equals(username)) {
                    categories.remove(categoryId, future);
                } else {
                    future.fail(new AuthorizationRequiredException());
                }
            } else {
                future.fail(get.cause());
            }
        });
        return future;
    }

    @Override
    public Future<FlashCategory> rate(String categoryId, String username, Integer rating) {
        Future<FlashCategory> future = Future.future();

        // run the check later - use a runnable to prevent deep nesting.
        Runnable addRatingToCategory = () -> {
            categories.get(categoryId, get -> {
                if (get.succeeded()) {
                    FlashCategory category = get.result();
                    category.setRateCount(category.getRateCount() + 1);
                    float delta = ((rating - category.getRating()) / category.getRateCount());
                    category.setRating(delta + category.getRating());
                    categories.update(category, updated -> {
                        if (updated.succeeded()) {
                            future.complete(category);
                        } else {
                            future.fail(updated.cause());
                        }
                    });
                } else {
                    future.fail(get.cause());
                }
            });
        };

        // check if allowed to vote.
        voters.addIfAllowedElseFail(new VoterEvent(username, categoryId)).setHandler(done -> {
            if (done.succeeded()) {
                addRatingToCategory.run();
            } else {
                future.fail(done.cause());
            }
        });
        return future;
    }
}
