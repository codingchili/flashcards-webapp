package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A set of flash cards that can be shared.
 */
public class FlashCategory implements Storable {
    public static final String ID_SHARED = "shared";
    public static final String ID_USERS = "users";
    private static final int MAX_SCORE_HISTORY = 10;
    private boolean shared = false;
    private String owner;
    private String name;
    private Set<String> users = new HashSet<>();
    private List<FlashScore> highscores = new ArrayList<>();

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

    public FlashCategory shareWith(Collection<String> users) {
        this.users.addAll(users);
        return this;
    }

    public FlashCategory unauthorize(String user) {
        users.remove(user);
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
        return owner + "_" + name;
    }
}
