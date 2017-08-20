package com.codingchili.flashcards.response;

/**
 * Response class to wrap primitive.
 */
public class SizeResponse {
    private int size;

    public SizeResponse(Integer size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
