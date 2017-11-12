package com.codingchili.flashcards.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Used for sending messages between users.
 */
public class AccountMessage {
    private String id = UUID.randomUUID().toString();
    private Long created = Instant.now().getEpochSecond();
    private boolean read = false;
    private String sender;
    private String title;
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public AccountMessage setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AccountMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public AccountMessage setBody(String body) {
        this.body = body;
        return this;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
