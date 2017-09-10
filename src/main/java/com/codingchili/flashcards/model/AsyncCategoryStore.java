package com.codingchili.flashcards.model;

import io.vertx.core.Future;

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
     * Lists all categories either owned or shared with the given user.
     *
     * @param username limits the results to categories available
     *                 to the given user.
     * @return callback
     */
    Future<Collection<FlashCategory>> search(String query, String username);

    /**
     * Performs a search without using a user context, returns public results only.
     *
     * @return callback
     */
    Future<Collection<FlashCategory>> search(String query);

    /**
     * Lists categories owned by the given username.
     * @param username the username of the owner.
     *
     * @return callback
     */
    Future<Collection<FlashCategory>> list(String username);

    /**
     * Lists the number of categories in the database.
     *
     * @return callback
     */
    Future<Integer> size();
}
