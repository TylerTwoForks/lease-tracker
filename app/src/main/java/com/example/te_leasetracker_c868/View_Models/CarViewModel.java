package com.example.te_leasetracker_c868.View_Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.Database.AppRepository;

import java.util.List;

public class CarViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    public CarViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public void updateCar(Car car){ appRepository.updateCar(car); }
    public void insertCar(Car car){ appRepository.insertCar(car); }
    public void deleteCar(Car car){ appRepository.deleteCar(car); }

    public LiveData<List<Car>> getCarsById(int id){
        return appRepository.getCarsByUserId(id);
    }

    public List<Car> getCarsForReport(int carId){
        return appRepository.getCarsForReport(carId);
    }

    public LiveData<List<Car>> searchCar(String carName, int userId){
        return appRepository.searchCar(carName, userId);
    }
}
