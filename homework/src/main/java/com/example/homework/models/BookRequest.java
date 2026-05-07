package com.example.homework.models;

import java.util.List;

public class BookRequest {
    private String title;
    private String author;
    private List<String> themes;
    private String readingLevel;

    public BookRequest() {}

    public BookRequest(String title, String author, List<String> themes, String readingLevel) {
        this.title = title;
        this.author = author;
        this.themes = themes;
        this.readingLevel = readingLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }

    public String getReadingLevel() {
        return readingLevel;
    }

    public void setReadingLevel(String readingLevel) {
        this.readingLevel = readingLevel;
    }
}