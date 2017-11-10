package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A set of flash cards that can be shared.
 */
public class FlashCategory implements Storable {
    public static final String ID_COST = "cost";
    public static final String ID_SHARED = "shared";
    public static final String ID_INDEXED_NAME = "indexedName";
    public static final String ID_USERS = "users";
    private static final int MAX_SCORE_HISTORY = 10;
    private float rating = 0.0f;
    private Integer rateCount = 0;
    private List<FlashScore> highscores = new ArrayList<>();
    private Set<String> users = new HashSet<>();
    private String id = UUID.randomUUID().toString();
    private SimpleDate created;
    private boolean shared = false;
    private String owner;
    private int cost = 0;
    private String name;
    private String indexedName;
    private String color = "var(--paper-pink-200)";
    private String icon = "apps";

    public String getOwner() {
        return owner;
    }

    public FlashCategory setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public Collection<String> getUsers() {
        return users;
    }

    public FlashCategory setUsers(Set<String> users) {
        this.users = users;
        return this;
    }

    public boolean isShared() {
        return shared;
    }

    public FlashCategory setShared(boolean shared) {
        this.shared = shared;
        return this;
    }

    public String getName() {
        return name;
    }

    public FlashCategory setName(String name) {
        this.indexedName = name.toLowerCase();
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<FlashScore> getHighscores() {
        return highscores;
    }

    public void setHighscores(List<FlashScore> highscores) {
        this.highscores = highscores;
    }

    public FlashCategory score(String user, int correct, int misses, int seconds) {
        highscores.add(new FlashScore()
                .setUser(user)
                .setCorrect(correct)
                .setMisses(misses)
                .setSeconds(seconds));

        highscores = highscores.stream()
                .sorted(Storable::compareTo)
                .limit(MAX_SCORE_HISTORY)
                .collect(Collectors.toList());
        return this;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public void setIndexedName(String indexedName) {
        this.indexedName = indexedName;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this.compareTo(other) == 0;
    }

    public SimpleDate getCreated() {
        return created;
    }

    public void setCreated(SimpleDate created) {
        this.created = created;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Integer getRateCount() {
        return rateCount;
    }

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }
}
