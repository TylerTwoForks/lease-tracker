package com.example.te_leasetracker_c868.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.DB_Entities.User;
import com.example.te_leasetracker_c868.Utility.PopDb;

@androidx.room.Database(entities = {User.class, Car.class}, version = 2)
@TypeConverters(DateConverter.class)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract UserDao userDao();
    public abstract CarDao carDao();

    public static synchronized Database getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db){
          super.onCreate(db);
          new PopulateDbAsyncTask(instance).execute();
      }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private UserDao userDao;
        private CarDao carDao;

        private PopulateDbAsyncTask(Database db){
            userDao = db.userDao();
            carDao = db.carDao();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            userDao.popUsers(PopDb.popUsers());
            carDao.popCars(PopDb.popCars());
            return null;
        }
    }
}
