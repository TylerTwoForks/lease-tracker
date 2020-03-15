package com.example.te_leasetracker_c868.DB_Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "car_table",
        foreignKeys = @ForeignKey(entity = User.class, childColumns = "fk_user_id", parentColumns = "user_id"))
public class Car {
    @PrimaryKey(autoGenerate = true)
    private int car_id;

    private String car_name;
    private LocalDate lease_start;
    private String car_make;
    private String car_model;
    private int starting_mileage;
    private int mileage_allowed;
    private int fk_user_id;

    public Car(String car_name, LocalDate lease_start, String car_make, String car_model, int starting_mileage, int mileage_allowed, int fk_user_id) {
        this.car_name = car_name;
        this.lease_start = lease_start;
        this.car_make = car_make;
        this.car_model = car_model;
        this.starting_mileage = starting_mileage;
        this.mileage_allowed = mileage_allowed;
        this.fk_user_id = fk_user_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public LocalDate getLease_start() {
        return lease_start;
    }

    public String getCar_make() {
        return car_make;
    }

    public String getCar_model() {
        return car_model;
    }

    public int getCar_starting_mileage() {
        return starting_mileage;
    }

    public int getMileage_allowed() {
        return mileage_allowed;
    }

    public int getFk_user_id() { return fk_user_id; }

    public int getStarting_mileage() { return starting_mileage; }



}
