package com.example.te_leasetracker_c868.Utility;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();

    //static String dateFormattedShort;

    //This takes 3 entries and converts to Date format.
    public static String dateConverter(int year, int month, int day){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {month += 1;}

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateString = month + "/" + day + "/" + year;
        Date dateFormattedLong = null;
        try {
            dateFormattedLong = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateFormattedLong != null;
        return sdf.format(dateFormattedLong);

    }


    //Takes String format and parses to date.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate stringToDateConverter(String dateString){
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public static Date stringToDateConverterOld(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date dateFormattedLong = null;
        try{
            dateFormattedLong = sdf.parse(dateString);
        }catch (ParseException pe){
            pe.printStackTrace();
        }

        return dateFormattedLong;
    }

    public static String dateToString(LocalDate date){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return date.format(formatter);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            return sdf.format(date);
        }

    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Boolean compareDates(Date date1, Date date2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return sdf.format(date1).equals(sdf.format(date2));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate formatLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(date, formatter);

    }
}

