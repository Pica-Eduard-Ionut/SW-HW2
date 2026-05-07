package com.example.homework.models;

public class User {
    private String name;
    private String readingLevel;
    private String preferredTheme;

    public User() {}

    public User(String name, String readingLevel, String preferredTheme) {
        this.name = name;
        this.readingLevel = readingLevel;
        this.preferredTheme = preferredTheme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReadingLevel() {
        return readingLevel;
    }

    public void setReadingLevel(String readingLevel) {
        this.readingLevel = readingLevel;
    }

    public String getPreferredTheme() {
        return preferredTheme;
    }

    public void setPreferredTheme(String preferredTheme) {
        this.preferredTheme = preferredTheme;
    }

    @Override
    public String toString() {
        return "User{name='" + name + '\'' + ", readingLevel='" + readingLevel + '\'' + ", preferredTheme='" + preferredTheme + '\'' + '}';
    }
}