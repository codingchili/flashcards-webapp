package com.codingchili.flashcards.model;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.Collection;

/**
 * Interface for a category manager/db.
 */
public interface AsyncCategoryStore {
    /**
     * Retrieves the category by its name.
     * @param category name of the category to retrieve.
     * @return callback
     */
    Future<FlashCategory> get(String category, String username);

    /**
     * Adds a category to the database or updates if existing.
     *
     * @param category the category to be saved
     * @return callback
     */
    Future<Void> save(FlashCategory category);

    /**
     * Lists all categories publicly available.
     *
     * @return callback
     */
    void shared(Handler<AsyncResult<Collection<FlashCategory>>> handler);

    /**
     * Lists all categories either owned or shared with the given user.
     *
     * @param username limits the results to categories available
     *                 to the given user.
     * @return callback
     */
    Future<Collection<FlashCategory>> available(String username);

    /**
     * Lists the number of categories in the database.
     *
     * @return callback
     */
    Future<Integer> size();
}
