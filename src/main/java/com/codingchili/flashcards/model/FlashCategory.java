package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A set of flash cards that can be shared.
 */
public class FlashCategory implements Storable {
    public static final String ID_COST = "cost";
    public static final String ID_SHARED = "shared";
    public static final String ID_USERS = "users";
    private static final int MAX_SCORE_HISTORY = 10;
    private List<FlashScore> highscores = new ArrayList<>();
    private Set<String> users = new HashSet<>();
    private String id = UUID.randomUUID().toString();
    private ZonedDateTime created;
    private boolean shared = false;
    private String owner;
    private int cost = 0;
    private String name;
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

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getCreated() {
        return created;
    }
}
