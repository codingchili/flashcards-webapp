package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.protocol.exception.AuthorizationRequiredException;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.AppConfig;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.Collection;

import static com.codingchili.core.configuration.CoreStrings.ID_NAME;
import static com.codingchili.flashcards.model.FlashCard.ID_OWNER;
import static com.codingchili.flashcards.model.FlashCategory.*;
import static java.util.stream.Collectors.toList;

/**
 * Handles storage of categories.
 */
public class CategoryDB implements AsyncCategoryStore {
    private static final String ARRAY = "[]";
    private AsyncStorage<FlashCategory> categories;

    public CategoryDB(CoreContext core) {
        new StorageLoader<FlashCategory>(core)
                .withPlugin(AppConfig.storage())
                .withClass(FlashCategory.class)
                .withDB(AppConfig.db(), "categories")
                .build(done -> categories = done.result());
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
        categories.query(ID_OWNER).equalTo(username).execute(future);
        return future;
    }

    @Override
    public Future<Collection<FlashCategory>> search(String username, String query) {
        Future<Collection<FlashCategory>> future = Future.future();
        categories
                .query(ID_OWNER).equalTo(username).and(ID_NAME).startsWith(query)
                .or(ID_USERS + ARRAY).equalTo(username).and(ID_NAME).startsWith(query)
                .or(ID_SHARED).equalTo(true).and(ID_NAME).startsWith(query)
                .or(ID_COST).between(1L, Long.MAX_VALUE).and(ID_NAME).startsWith(query)
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
        categories.query(ID_SHARED).equalTo(true).execute(future);
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

}
