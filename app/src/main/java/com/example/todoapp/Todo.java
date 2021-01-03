package com.example.todoapp;

public class Todo {
    //private UUID uuid = UUID.randomUUID();
    private String title;
    private String text;
    private String path;

    public Todo(String title, String text, String path) {
        this.title = title;
        this.text = text;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
