package com.example.minorprojectdriver;

public class Students {
    public String NAME;
    public String ID_NO;
    public String ROLL_NO;
    public String EMAIL_ID;
    public String PASSWORD;

    public Students() {
        // Default constructor required for Firebase Realtime Database
    }

    public Students(String NAME, String ID_NO, String ROLL_NO, String EMAIL_ID, String PASSWORD) {
        this.NAME = NAME;
        this.ID_NO = ID_NO;
        this.ROLL_NO = ROLL_NO;
        this.EMAIL_ID = EMAIL_ID;
        this.PASSWORD= PASSWORD;
    }
}

