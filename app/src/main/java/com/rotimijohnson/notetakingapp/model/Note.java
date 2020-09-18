package com.rotimijohnson.notetakingapp.model;

public class Note {
    private String title, note,username,password;
    private int id;

    public Note() {
    }

    public Note(int id, String title, String note) {
        this.title = title;
        this.note = note;
        this.id = id;
    }

    public Note(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public void usernamePassword(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
