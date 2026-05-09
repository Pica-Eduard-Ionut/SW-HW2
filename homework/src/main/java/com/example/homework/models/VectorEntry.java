package com.example.homework.models;

public class VectorEntry {
    private final String bookId;
    private final String text;
    private final float[] embedding;

    public VectorEntry(String bookId, String text, float[] embedding) {
        this.bookId = bookId;
        this.text = text;
        this.embedding = embedding;
    }

    public String getBookId() { return bookId; }
    public String getText() { return text; }
    public float[] getEmbedding() { return embedding; }
}
