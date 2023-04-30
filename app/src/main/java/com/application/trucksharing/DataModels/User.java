package com.application.trucksharing.DataModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Data model for users
 */

@Entity(tableName = "users", indices = {@Index(value = {"user_name"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "full_name")
    public String fullName;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "password")
    public String passWord;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "image_uri")
    public String imageUri;

    public User(String fullName, String userName, String passWord, String phoneNumber, String imageUri) {
        this.fullName = fullName;
        this.userName = userName;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }
}
