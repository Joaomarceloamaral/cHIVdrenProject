package com.example.chivdrenproject.model;

public class ModelUser {
    String name, email,date,uidemail;

    public ModelUser() {
    }

    public ModelUser(String name, String email, String date, String uidemail) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.uidemail = uidemail;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUidemail() {
        return uidemail;
    }

    public void setUidemail(String uidemail) {
        this.uidemail = uidemail;
    }
}
