package com.application.trucksharing.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.Repositories.DeliveryOrderRepository;
import java.util.List;

public class DeliveryOrderViewModel extends AndroidViewModel {

    // Delivery order repository that can be swapped out
    private DeliveryOrderRepository deliveryOrderRepository;

    // All delivery orders live data
    private final LiveData<List<DeliveryOrder>> allDeliveryOrders;

    // Because the new delivery order form is spread across two fragments, I decided to
    // have a variable here to hold a pending version of a DeliveryOrder instance.
    // Also,we can use this to hold the data between new orders fragment and order details
    private DeliveryOrder pendingNewDeliveryOrder = new DeliveryOrder();

    public DeliveryOrderViewModel(@NonNull Application application) {

        super(application);

        deliveryOrderRepository = new DeliveryOrderRepository(application);
        allDeliveryOrders = deliveryOrderRepository.getAllDeliveryOrders();
    }

    // Get the pending new delivery order so that it can be populated with data
    public DeliveryOrder getPendingNewDeliveryOrder() {

        return pendingNewDeliveryOrder;
    }

    // Get all delivery orders
    public LiveData<List<DeliveryOrder>> getAllDeliveryOrders() {

        return allDeliveryOrders;
    }

    // Instead of taking a new delivery order as a parameter,
    // this time, this will use the pending new delivery order instead.
    public void insertNewAvailableTruck(){

        deliveryOrderRepository.insertNewDeliveryOrder(pendingNewDeliveryOrder);
    }
}
