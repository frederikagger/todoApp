package com.example.todoapp;

import java.util.UUID;

public class Todo {
    //private UUID uuid = UUID.randomUUID();
    private String title;
    private String text;

    public Todo(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Todo(String text) {
        this.text = text;
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
