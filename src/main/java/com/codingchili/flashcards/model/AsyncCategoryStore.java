package com.codingchili.flashcards.model;

import io.vertx.core.Future;

import java.util.Collection;

/**
 * Interface for a category manager/db.
 */
public interface AsyncCategoryStore {
    /**
     * Retrieves the category by its name.
     *
     * @param id of the category to retrieve.
     * @return callback
     */
    Future<FlashCategory> get(String id);

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
    Future<Collection<FlashCategory>> search(String username, String query);

    /**
     * Performs a search without using a user context, returns public results only.
     *
     * @return callback
     */
    Future<Collection<FlashCategory>> search(String query);

    /**
     * Lists categories owned by the given username.
     *
     * @param username the username of the owner.
     * @return callback
     */
    Future<Collection<FlashCategory>> list(String username);

    /**
     * Lists the number of categories in the database.
     *
     * @return callback
     */
    Future<Integer> size();

    /**
     * Removes a category from the database.
     *
     * @param username   the owner of the category to remove
     * @param categoryId the id of the category to remove
     * @return callback
     */
    Future<Void> remove(String username, String categoryId);

    /**
     * Updates the rating of a category.
     *
     * @param categoryId the category id to rate on.
     * @param username the user updating the rating.
     * @param rating the rating to set.
     * N@return callback.
     */
    Future<FlashCategory> rate(String categoryId, String username, Integer rating);
}
