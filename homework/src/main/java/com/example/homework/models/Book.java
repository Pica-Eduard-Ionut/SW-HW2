package com.example.homework.models;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private List<String> themes;
    private String readingLevel;

    public Book() {
        this.themes = new ArrayList<>();
    }

    public Book(String title, String author, List<String> themes, String readingLevel) {
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

    @Override
    public String toString() {
        return "Book{title='" + title + '\'' + ", author='" + author + '\'' + ", themes=" + themes + ", readingLevel='" + readingLevel + '\'' + '}';
    }
}