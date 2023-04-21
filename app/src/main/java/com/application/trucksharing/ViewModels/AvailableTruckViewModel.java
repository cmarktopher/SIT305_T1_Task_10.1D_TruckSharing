package com.application.trucksharing.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.Database.TruckSharingDatabase;
import com.application.trucksharing.Repositories.AvailableTruckRepository;
import java.util.List;

public class AvailableTruckViewModel extends AndroidViewModel {

    // Our repository - can swap it out if we switch systems
    private AvailableTruckRepository availableTruckRepository;

    // Live data of our available trucks
    private final LiveData<List<AvailableTruck>> allAvailableTrucks;


    public AvailableTruckViewModel(@NonNull Application application) {

        super(application);

        availableTruckRepository = new AvailableTruckRepository(application);
        allAvailableTrucks = availableTruckRepository.getAllAvailableTrucks();
    }

    public LiveData<List<AvailableTruck>> getAllAvailableTrucks() {

        return allAvailableTrucks;
    }

    public void insertNewAvailableTruck(AvailableTruck newAvailableTruck){

        availableTruckRepository.insertNewAvailableTruck(newAvailableTruck);
    }

    public void deleteAvailableTruck(AvailableTruck availableTruck){

        availableTruckRepository.deleteAvailableTruck(availableTruck);
    }
}

