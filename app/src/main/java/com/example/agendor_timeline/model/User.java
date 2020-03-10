package com.example.agendor_timeline.model;

public class User {

    private String name;
    private String email;
    private String id;
    private String password;

    public User(String name, String email, String id, String password) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.password = password;
    }

    public String getSenha() {
        return password;
    }

    public void setSenha(String senha) {
        this.password = senha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
