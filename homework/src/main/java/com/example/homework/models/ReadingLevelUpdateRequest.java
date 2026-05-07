package com.example.homework.models;

public class ReadingLevelUpdateRequest {
    private String readingLevel;

    public ReadingLevelUpdateRequest() {}

    public ReadingLevelUpdateRequest(String readingLevel) {
        this.readingLevel = readingLevel;
    }

    public String getReadingLevel() {
        return readingLevel;
    }

    public void setReadingLevel(String readingLevel) {
        this.readingLevel = readingLevel;
    }
}