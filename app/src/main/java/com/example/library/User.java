package com.example.library;

public class User {
    private String email;
    private String password;
    private boolean isAdmin;

    public User() {
        // Обязательный конструктор без параметров для использования Firebase
    }

    public User(String email, String password, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
