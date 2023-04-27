package com.application.trucksharing.Repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.application.trucksharing.DataAccessObjects.DeliveryOrderDAO;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.Database.TruckSharingDatabase;
import java.util.List;

public class DeliveryOrderRepository {

    private Application currentApplication;
    private DeliveryOrderDAO deliveryOrderDAO;
    private LiveData<List<DeliveryOrder>> allDeliveryOrders;

    public DeliveryOrderRepository(Application application){

        currentApplication = application;
        deliveryOrderDAO = TruckSharingDatabase.getDatabase(application).deliveryOrderDAO();
        allDeliveryOrders = deliveryOrderDAO.getAllDeliveryOrders();
    }

    public LiveData<List<DeliveryOrder>> getAllDeliveryOrders() {
        return allDeliveryOrders;
    }

    public void insertNewDeliveryOrder(DeliveryOrder newDeliveryOrder){

        TruckSharingDatabase.databaseWriteExecutor.execute(() -> {

            deliveryOrderDAO.insertNewDeliveryOrder(newDeliveryOrder);
        });
    }
}
