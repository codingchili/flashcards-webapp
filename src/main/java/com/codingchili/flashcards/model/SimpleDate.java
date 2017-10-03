package com.codingchili.flashcards.model;

import java.time.ZonedDateTime;

/**
 * Simplest date format ever, used instead of ZonedDateTime which
 * does not serialize well.
 */
public class SimpleDate {
    private String month;
    private int day;
    private int year;

    public SimpleDate() {
        this(ZonedDateTime.now());
    }

    public SimpleDate(ZonedDateTime date) {
        this.year = date.getYear();
        this.month = date.getMonth().name();
        this.day = date.getDayOfMonth();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
