package com.codingchili.flashcards.model;

import com.codingchili.core.security.Account;

import java.util.LinkedList;

/**
 * Adds an inbox to the default account.
 */
public class FlashAccount extends Account {
    private LinkedList<AccountMessage> inbox = new LinkedList<>();

    public LinkedList<AccountMessage> getInbox() {
        return inbox;
    }

    public void setInbox(LinkedList<AccountMessage> inbox) {
        this.inbox = inbox;
    }

    public void addMessage(AccountMessage message) {
        inbox.addFirst(message);
    }

    public void readMessage(String messageId) {
        inbox.forEach(message -> {
            if (message.getId().equals(messageId))
                message.setRead(true);
        });
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public String toString() {
        return getId();
    }
}
