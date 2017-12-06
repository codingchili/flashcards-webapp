package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

import java.time.Instant;

/**
 * A model of a vote event - used to limit numbers of vote cast per time period.
 */
public class VoterEvent implements Storable {
    public static final String CREATED = "createdAt";
    private String user;
    private String category;
    private Long createdAt;

    public VoterEvent(String user, String category) {
        this.user = user;
        this.category = category;
        this.createdAt = Instant.now().getEpochSecond();
    }

    @Override
    public String id() {
        return user + "." + category;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long created) {
        this.createdAt = created;
    }

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }
}
