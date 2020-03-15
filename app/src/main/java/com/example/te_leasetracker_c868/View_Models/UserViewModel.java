package com.example.te_leasetracker_c868.View_Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.te_leasetracker_c868.DB_Entities.User;
import com.example.te_leasetracker_c868.Database.AppRepository;

public class UserViewModel extends AndroidViewModel {
    private AppRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void insertUser(User user) {
        repository.insertUser(user);
    }

    public void deleteUser(User user) {
        repository.deleteUser(user);
    }

    public void updateUser(User user) {
        repository.updateUser(user);
    }

    public boolean checkLogin(String userName, String pass) {
        return repository.isValidAccount(userName, pass);
    }

    public User getUserByName(String un){return repository.getUsersByName(un);}

    public boolean checkUser(String un){ return repository.checkUser(un); }

}
