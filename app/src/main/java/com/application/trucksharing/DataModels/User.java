package com.application.trucksharing.DataModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data model for users
 */

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "full_name")
    public String fullName;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "pass_word")
    public String passWord;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;
}
