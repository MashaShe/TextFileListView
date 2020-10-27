package com.example.textfilelistview;

import android.widget.Button;

public class MyData {
    public String header;
    public String text;
    public String author;
    public Button button;

    public MyData(String header, String text, String author) {
        this.header = header;
        this.text = text;
        this.author = author;
    }

    @Override
    public String toString() {
        return  header + "'\n\n'" + text + "'\n\n'" + author + "'\n\n'";
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

}
