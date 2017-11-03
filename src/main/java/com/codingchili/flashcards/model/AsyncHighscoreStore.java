package com.codingchili.flashcards.model;

import io.vertx.core.Future;

/**
 * Interface used for asynchronous highscore storage.
 */
public interface AsyncHighscoreStore {

    /**
     * Add a new highscore to a list.
     * @param category the category to add the highscore entry to.
     * @param highscore contains the category to which the highscore is to be recorded in.
     * @return callback
     */
    Future<HighscoreList> add(String category, Highscore highscore);

    /**
     * retrieve a list of highscores for the given category.
     * @return callback
     */
    Future<HighscoreList> list(String category);
}
