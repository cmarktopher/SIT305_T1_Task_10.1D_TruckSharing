package com.application.trucksharing.DataAccessObjects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.application.trucksharing.DataModels.User;

@Dao
public interface UsersDAO {

    @Query("SELECT * FROM users WHERE user_name = :userName")
    void getUserByUserName(String userName);

    @Insert
    void insertNewUser(User newUser);
}
