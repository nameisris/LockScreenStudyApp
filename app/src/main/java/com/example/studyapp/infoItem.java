package com.example.studyapp;

public class infoItem {
    private int i = 0;
    private String no;
    private String userID;
    private String userPassword;
    private String GropName;
    private String Introduce;
    private String Personnel;
    private String GropPassword;

    public int geti() {
        return i;
    }

    public String getno() {
        return no;
    }

    public String getuserID() {
        return userID;
    }

    public String getuserPassword() {
        return userPassword;
    }

    public String getGropName() {
        return GropName;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public String getPersonnel() {
        return Personnel;
    }

    public String getGropPassword() {
        return GropPassword;
    }

    public void seti(int i) {
        this.i = i;
    }

    public void setno(String no) {
        this.no = no;
    }

    public void setuserID(String userID) {
        this.userID = userID;
    }

    public void setuserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setname(String GropName) {
        this.GropName = GropName;
    }

    public void setgrop(String Introduce) {
        this.Introduce = Introduce;
    }

    public void setPersonnel(String Personnel) {
        this.Personnel = Personnel;
    }

    public void setGropPassword(String GropPassword) {
        this.GropPassword = GropPassword;
    }

}