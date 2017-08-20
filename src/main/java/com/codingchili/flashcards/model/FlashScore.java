package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

/**
 * Created by robdu on 2017-08-19.
 */
public class FlashScore implements Storable {
    private String user;
    private int correct;
    private int misses;
    private int seconds;

    public String getUser() {
        return user;
    }

    public FlashScore setUser(String user) {
        this.user = user;
        return this;
    }

    public int getCorrect() {
        return correct;
    }

    public FlashScore setCorrect(int correct) {
        this.correct = correct;
        return this;
    }

    public int getMisses() {
        return misses;
    }

    public FlashScore setMisses(int misses) {
        this.misses = misses;
        return this;
    }

    public int getSeconds() {
        return seconds;
    }

    public FlashScore setSeconds(int seconds) {
        this.seconds = seconds;
        return this;
    }

    @Override
    public int compareTo(Object other) {
        FlashScore score = (FlashScore) other;
        if (score.getCorrect() > correct) {
            return -1;
        } else if (score.getCorrect() == correct) {
            return 0;
        } else {
            return 1;
        }
    }
}
