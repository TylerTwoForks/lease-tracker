package com.example.te_leasetracker_c868.DB_Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int user_id;

    private String user_name;
    private String user_pw;

    public User(String user_name, String user_pw) {
        this.user_name = user_name;
        this.user_pw = user_pw;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_pw() {
        return user_pw;
    }


}
