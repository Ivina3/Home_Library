package com.example.library;

public class Book {
    private String title;
    private String author;
    private String year;
    private String imageUrl;
    private String description;
    private String bookTextUrl;  // Добавляем поле для URL текста книги

    public Book() {
        // Обязательный конструктор для использования Firebase
    }

    public Book(String title, String author, String year, String imageUrl, String description, String bookTextUrl) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.imageUrl = imageUrl;
        this.description = description;
        this.bookTextUrl = bookTextUrl;  // Инициализация поля
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getBookTextUrl() {
        return bookTextUrl;
    }
}
