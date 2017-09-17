package com.codingchili.flashcards.model;

import io.vertx.core.Future;

import java.util.Collection;

/**
 * Interface to store/retrieve/update cards in the storage.
 */
public interface AsyncCardStore {
    /**
     * Adds a new card to the given collection.
     *
     * @param card the card to be added.
     */
    Future<Void> add(FlashCard card);

    /**
     * Removes the given card.
     *
     * @param cardId id of the card to be removed
     */
    Future<Void> remove(String cardId);

    /**
     * Gets all cards in the given category.
     *
     * @param category the category to list cards from.
     */
    Future<Collection<FlashCard>> get(String username, FlashCategory category);

    /**
     * Calculates the number of cards in the database.
     * @return callback
     */
    Future<Integer> size();
}
