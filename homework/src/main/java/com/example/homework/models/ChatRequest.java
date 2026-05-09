package com.example.homework.models;

public class ChatRequest {
    private String message;
    private String pageContext;
    private String bookId;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPageContext() { return pageContext; }
    public void setPageContext(String pageContext) { this.pageContext = pageContext; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
}
