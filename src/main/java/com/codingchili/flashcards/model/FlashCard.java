package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contains model data for a flash card.
 */
public class FlashCard implements Storable {
    public static final String ID_CATEGORY = "category";
    public static final String ID_CONTENT = "content";
    public static final String ID_ANSWER = "answer";
    public static final String ID_OWNER = "owner";
    private String id = UUID.randomUUID().toString();
    private List<String> alternatives = new ArrayList<>();
    private String owner;
    private String category;
    private String content;
    private String back;
    private String answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public FlashCard setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public FlashCard setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getContent() {
        return content;
    }

    public FlashCard setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public FlashCard setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public FlashCard setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getBack() {
        return back;
    }

    public FlashCard setBack(String back) {
        this.back = back;
        return this;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
