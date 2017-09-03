package com.codingchili.flashcards.model;

import com.codingchili.core.security.Account;
import com.codingchili.core.security.Token;
import io.vertx.core.Future;

import java.util.Collection;

/**
 * Interface used to handle authentication and the storage of users.
 */
public interface AsyncAccountStore {
    /**
     * Authenticates the given username:password combination.
     *
     * @param username the username to authenticate as
     * @param password the password for the given user
     * @return callback
     */
    Future<Token> authenticate(String username, String password);

    /**
     * Registers a new account if not already existing.
     *
     * @param account the account to register
     * @return callback
     */
    Future<Token> register(Account account);

    /**
     * Searches for users, useful when sharing categories.
     * @param username the query string, must match the beginning.
     * @return a collection of users with an username that resembles the given
     */
    Future<Collection<Account>> search(String username);

    /**
     * Gets the number of accounts in the system.
     * @return callback
     */
    Future<Integer> size();
}
