package com.application.trucksharing.DataAccessObjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.application.trucksharing.DataModels.User;

import java.util.List;

@Dao
public interface UsersDAO {

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE user_name = :userName")
    User getUserByUserName(String userName);

    @Insert
    void insertNewUser(User newUser);
}
