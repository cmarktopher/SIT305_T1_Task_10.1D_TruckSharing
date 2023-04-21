package com.application.trucksharing.DataAccessObjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.application.trucksharing.DataModels.AvailableTruck;
import java.util.List;

@Dao
public interface AvailableTruckDAO {

    @Query("SELECT * FROM available_trucks")
    LiveData<List<AvailableTruck>> getAllAvailableTrucks();

    @Insert
    void insertNewAvailableTruck(AvailableTruck newAvailableTruck);

    @Delete
    void deleteAvailableTruck(AvailableTruck availableTruck);

    @Query("DELETE FROM available_trucks")
    void deleteAll();
}

