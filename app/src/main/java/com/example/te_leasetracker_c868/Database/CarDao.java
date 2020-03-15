package com.example.te_leasetracker_c868.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.DB_Entities.User;

import java.util.List;

@Dao
public interface CarDao {
    @Insert
    void insertCar(Car car);
    
    @Insert
    void popCars(List<Car> cars);
    
    @Update
    void updateCar(Car car);

    @Delete
    void deleteCar(Car car);

    @Query("DELETE FROM car_table")
    void deleteAllCars();

    @Query("SELECT * FROM car_table")
    LiveData<List<Car>> getAllCars();

    @Query("SELECT * FROM car_table WHERE fk_user_id = :userId")
    List<Car> getCarsForReport(final int userId);

    @Query("SELECT * FROM car_table WHERE fk_user_id = :userId")
    LiveData<List<Car>> getCarsByUserId(final int userId);

    @Query("SELECT * FROM car_table WHERE car_name LIKE :carName AND fk_user_id = :userId")
    LiveData<List<Car>> searchCar(final String carName, final int userId);

    @Query("SELECT COUNT(*) FROM car_table")
    LiveData<Integer> getCarCount();

}
