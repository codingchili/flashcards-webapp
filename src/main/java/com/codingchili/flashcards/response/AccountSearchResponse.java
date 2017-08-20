package com.codingchili.flashcards.response;

import java.util.Collection;

/**
 * Response class to wrap list.
 */
public class AccountSearchResponse {
    private Collection<String> users;

    public AccountSearchResponse(Collection<String> users) {
        this.users = users;
    }

    public Collection<String> getUsers() {
        return users;
    }

    public void setUsers(Collection<String> users) {
        this.users = users;
    }
}
