package com.application.trucksharing.Repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.application.trucksharing.DataAccessObjects.AvailableTruckDAO;
import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.Database.TruckSharingDatabase;
import java.util.List;

/**
 * Repository for available trucks
 * We will be use live data for this one so that our recycler view can be kept up to date when a truck is/not available
 */
public class AvailableTruckRepository {

    private Application currentApplication;
    private AvailableTruckDAO availableTruckDAO;
    private LiveData<List<AvailableTruck>> allAvailableTrucks;

    public AvailableTruckRepository(Application application){

        currentApplication = application;
        availableTruckDAO = TruckSharingDatabase.getDatabase(application).availableTruckDAO();
        allAvailableTrucks = availableTruckDAO.getAllAvailableTrucks();
    }

    public LiveData<List<AvailableTruck>> getAllAvailableTrucks() {
        return allAvailableTrucks;
    }

    public void insertNewAvailableTruck(AvailableTruck newAvailableTruck){

        TruckSharingDatabase.databaseWriteExecutor.execute(() -> {
            availableTruckDAO.insertNewAvailableTruck(newAvailableTruck);
        });
    }

    public void deleteAvailableTruck(AvailableTruck availableTruck){

        TruckSharingDatabase.databaseWriteExecutor.execute(() -> {
            availableTruckDAO.deleteAvailableTruck(availableTruck);
        });
    }

}
