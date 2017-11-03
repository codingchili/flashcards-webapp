package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;
import com.codingchili.flashcards.request.HighscoreRequest;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains a single highscore entry.
 *
 * If the version is changed because the algorithm has changed
 * all highscore entries on a single category will be removed
 * once a Highscore element using the new version is added.
 */
public class Highscore implements Storable {
    public static final double VERSION = 1.0;
    private String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    private Double version = VERSION;
    private String time;
    private String user;
    private Integer score;

    public static Highscore of(HighscoreRequest request) {
        Integer score = score(request.getCorrect(), request.getWrong(), request.getCards(), request.getSeconds());
        Highscore highscore = new Highscore();
        highscore.setScore(score);
        highscore.setUser(request.getUserId());
        highscore.setTime(formatMMSS(request.getSeconds()));
        return highscore;
    }

    private static Integer score(int correct, int wrong, int cards, int seconds) {
        // todo generate score by calculating number of correct answers
        // todo: reduce with penalty for errors
        // todo: recuce with penalty for time / total
        return 1;
    }

    public static void main(String[] args) {
        System.out.println(score(1, 0, 30, 1));
    }

    private static String formatMMSS(Integer seconds) {
        return (seconds / 60) + ":" + (seconds % 60);
    }

    public String getUser() {
        return user;
    }

    @Override
    public String id() {
        return user;
    }

    public Highscore setUser(String user) {
        this.user = user;
        return this;
    }

    public Integer getScore() {
        return score;
    }

    public Highscore setScore(Integer score) {
        this.score = score;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Highscore setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Highscore setTime(String time) {
        this.time = time;
        return this;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
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
