package com.example.te_leasetracker_c868.Database;

import android.os.Build;

import java.time.LocalDate;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

// example converter for java.util.Date
public class DateConverter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDate toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}