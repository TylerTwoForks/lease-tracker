package com.example.te_leasetracker_c868.Utility;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.DB_Entities.User;

import java.util.ArrayList;
import java.util.List;

public class PopDb {

    public static List<User> popUsers(){
        List<User> users = new ArrayList<>();
        users.add(new User("Test", "test"));
        users.add(new User("Test2", "test2"));
        return users;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Car> popCars(){
        List<Car> cars = new ArrayList<>();
        String leaseStartDate1 = "01/01/2020";
        cars.add(new Car(
                "My Car 1",
                DateUtil.stringToDateConverter(leaseStartDate1),
                "Toyota",
                "Camry",
                1200,
                12000,
                1));
        cars.add(new Car(
                "My Car 2",
                DateUtil.stringToDateConverter(leaseStartDate1),
                "Chevy",
                "Corvette",
                2400,
                12000,
                2));

        return cars;
    }
}
