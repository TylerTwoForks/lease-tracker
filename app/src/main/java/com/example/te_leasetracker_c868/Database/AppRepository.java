package com.example.te_leasetracker_c868.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.DB_Entities.User;

import java.util.List;

public class AppRepository {
    private static final String TAG = AppRepository.class.getSimpleName();

    private UserDao userDao;
    private CarDao carDao;

    private LiveData<List<User>> allUserLiveData;
    private LiveData<List<Car>> allCarLiveData;


    public AppRepository(Application application) {
        Database database = Database.getInstance(application);

        userDao = database.userDao();
        carDao = database.carDao();

        //getting entries for each table
        allUserLiveData = userDao.getAllUsers();
        allCarLiveData = carDao.getAllCars();
    }

    ////////////User modifications accessible by the rest of the app instead of accessing Dao directly////////////
    public void insertUser(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void updateUser(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void deleteUser(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUserLiveData;
    }

    public User getUsersByName(String userName) {
        return userDao.getUserByName(userName);
    }

    public LiveData<List<User>> getUsersById(int userId) {
        return userDao.getUserById(userId);
    }

    ////////////Car modifications accessible by the rest of the app instead of accessing Dao directly////////////
    public void insertCar(Car car) {
        new InsertCarAsyncTask(carDao).execute(car);
    }

    public void updateCar(Car car) {
        new UpdateCarAsyncTask(carDao).execute(car);
    }

    public void deleteCar(Car car) {
        new DeleteCarAsyncTask(carDao).execute(car);
    }

    public LiveData<List<Car>> getAllCars() {
        return allCarLiveData;
    }

    public LiveData<List<Car>> searchCar(String carName, int userId) {
        return carDao.searchCar(carName, userId);
    }

    public List<Car> getCarsForReport(int userId){
        return carDao.getCarsForReport(userId);
    }

    public LiveData<List<Car>> getCarsByUserId(int userId) {
        return carDao.getCarsByUserId(userId);
    }

    ////////////User AsyncTasks/////////////////
    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.updateUser(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser(users[0]);
            return null;
        }
    }

    ////////////Car AsyncTasks/////////////////
    private static class InsertCarAsyncTask extends AsyncTask<Car, Void, Void> {
        private CarDao carDao;

        private InsertCarAsyncTask(CarDao carDao) {
            this.carDao = carDao;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            carDao.insertCar(cars[0]);
            return null;
        }
    }

    private static class UpdateCarAsyncTask extends AsyncTask<Car, Void, Void> {
        private CarDao carDao;

        private UpdateCarAsyncTask(CarDao carDao) {
            this.carDao = carDao;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            carDao.updateCar(cars[0]);
            return null;
        }
    }

    private static class DeleteCarAsyncTask extends AsyncTask<Car, Void, Void> {
        private CarDao carDao;

        private DeleteCarAsyncTask(CarDao carDao) {
            this.carDao = carDao;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            carDao.deleteCar(cars[0]);
            return null;
        }
    }


    public boolean isValidAccount(String username, final String password) {
        try {
            User user = userDao.getUserByName(username);
            return user.getUser_pw().equals(password);
        } catch (Exception e) {
            Log.d(TAG, "isValidAccount: UserName does not exist");
            e.printStackTrace();
        }

        return false;
    }

    public boolean checkUser(String un) {
        try {
            if (userDao.checkUser(un)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
