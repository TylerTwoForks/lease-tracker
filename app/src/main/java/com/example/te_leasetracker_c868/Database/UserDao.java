package com.example.te_leasetracker_c868.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_leasetracker_c868.DB_Entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Insert
    void popUsers(List<User> users);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE user_id = :userId")
    LiveData<List<User>> getUserById(final int userId);

    @Query("SELECT * FROM user_table WHERE user_name LIKE LOWER(:username)")
//    LiveData<List<User>> getUserByName(final String username);
    User getUserByName(final String username);

    @Query("SELECT * FROM user_table WHERE user_name LIKE LOWER(:username)")
//    LiveData<List<User>> getUserByName(final String username);
    boolean checkUser(final String username);

//////////Will possibly use these in the future. Leaving for now////////////
    @Query("SELECT COUNT(*) FROM user_table")
    LiveData<Integer> getUserCount();

    @Query("DELETE FROM user_table")
    void deleteAllUsers();


}
