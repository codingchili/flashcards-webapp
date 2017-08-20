package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.AppConfig;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.Collection;

import static com.codingchili.flashcards.model.FlashCard.ID_OWNER;
import static com.codingchili.flashcards.model.FlashCategory.ID_SHARED;
import static com.codingchili.flashcards.model.FlashCategory.ID_USERS;
import static java.util.stream.Collectors.toList;

/**
 * Handles storage of categories.
 */
public class CategoryDB implements AsyncCategoryStore {
    public static final String ARRAY = "[]";
    private AsyncStorage<FlashCategory> categories;

    public CategoryDB(CoreContext core) {
        new StorageLoader<FlashCategory>(core)
                .withPlugin(AppConfig.storage())
                .withClass(FlashCategory.class)
                .withDB(AppConfig.db(), "categories")
                .build(done -> categories = done.result());
    }

    @Override
    public Future<FlashCategory> get(String category, String username) {
        Future<FlashCategory> future = Future.future();
        categories.get(getId(category, username), future);
        return future;
    }

    private String getId(String category, String username) {
        return new FlashCategory()
                .setName(category)
                .setOwner(username)
                .id();
    }

    @Override
    public Future<Void> save(FlashCategory category) {
        Future<Void> future = Future.future();
        categories.put(category, future);
        return future;
    }

    @Override
    public void shared(Handler<AsyncResult<Collection<FlashCategory>>> handler) {
        categories.query(ID_SHARED).equalTo(true).execute(handler);
    }

    @Override
    public Future<Collection<FlashCategory>> available(String username) {
        Future<Collection<FlashCategory>> future = Future.future();
        categories.query(ID_OWNER)
                .equalTo(username)
                .or(ID_USERS + ARRAY)
                .equalTo(username)
                .execute(categories -> {
                    if (categories.succeeded()) {
                        future.complete(categories.result().stream()
                                .distinct()
                                .collect(toList()));
                    } else {
                        future.fail(categories.cause());
                    }
                });
        return future;
    }

    @Override
    public Future<Integer> size() {
        Future<Integer> future = Future.future();
        categories.size(future);
        return future;
    }

}
