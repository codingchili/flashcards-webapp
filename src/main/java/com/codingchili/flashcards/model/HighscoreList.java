package com.codingchili.flashcards.model;

import com.codingchili.core.storage.Storable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains the top scores for a single category.
 */
public class HighscoreList implements Storable {
    public static final String ID_SCORE = "score";
    private static final int MAX_SIZE = 10;
    private List<Highscore> list = new ArrayList<>();
    private String category;

    @Override
    public String getId() {
        return category;
    }

    @Override
    public int hashCode() {
        return category.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    public String getCategory() {
        return category;
    }

    public HighscoreList setCategory(String category) {
        this.category = category;
        return this;
    }

    public List<Highscore> getList() {
        return list;
    }

    public void setList(List<Highscore> list) {
        this.list = list;
    }

    public HighscoreList add(Highscore highscore) {
        // with some magical logic in the data objects we can improve the
        // performance of the database backend.
        list.add(highscore);
        list = list.stream()
                .filter(h -> h.getVersion().equals(Highscore.VERSION))
                .sorted((a, b)-> (b.getScore().compareTo(a.getScore())))
                .distinct()
                .limit(MAX_SIZE)
                .collect(Collectors.toList());
        return this;
    }
}
