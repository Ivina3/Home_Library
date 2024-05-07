package com.example.library;
public class Book {
    private String author;
    private String year;
    private int image;

    public Book(String author, String year, int image) {
        this.author = author;
        this.year = year;
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public int getImage() {
        return image;
    }
}
