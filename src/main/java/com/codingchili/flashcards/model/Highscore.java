package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;
import com.codingchili.flashcards.request.HighscoreRequest;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains a single highscore entry.
 * <p>
 * If the version is changed because the algorithm has changed
 * all highscore entries on a single category will be removed
 * once a Highscore element using the new version is added.
 */
public class Highscore implements Storable {
    static final double VERSION = 1.1;
    private static final int TIME_BONUS_AMPLIFIER = 50;
    private static final int PENALTY_WRONG_WEIGHT = 40;
    private static final int SCORE_CORRECT_WEIGHT = 100;
    private static final int MAX_BONUS_SECONDS = 60;
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
        int score = correct * SCORE_CORRECT_WEIGHT;
        score -= wrong * PENALTY_WRONG_WEIGHT;

        // calculate a diminishing bonus over the time to finish.
        double timeBonus = (Math.log((correct * MAX_BONUS_SECONDS) / (seconds + 1.0f)) * TIME_BONUS_AMPLIFIER);

        if (timeBonus > 0) {
            score += timeBonus;
        }
        return score;
    }

    private static String formatMMSS(Integer totalSeconds) {
        int seconds = totalSeconds % 60;
        return (totalSeconds / 60) + ":" + ((seconds > 9) ? seconds : "0" + seconds);
    }

    public String getUser() {
        return user;
    }

    @Override
    public String getId() {
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
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }
}
