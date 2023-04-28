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

    // My way of keeping track of the order we have selected when wanting to view the an order
    private DeliveryOrder currentSelectedOrder;

    public DeliveryOrderViewModel(@NonNull Application application) {

        super(application);

        deliveryOrderRepository = new DeliveryOrderRepository(application);
        allDeliveryOrders = deliveryOrderRepository.getAllDeliveryOrders();
    }

    /**
     * Get the pending new delivery order so that it can be populated with data
     */
    public DeliveryOrder getPendingNewDeliveryOrder() {

        return pendingNewDeliveryOrder;
    }

    /**
     * Getter and setter for the current selected order
     * Just a simple way to get our order details within a new fragment
     * Open to suggestions if there are better ways to do this without having to re-query the database for a specific entry.
     * That being said, perhaps re-querying via an id is the best approach?
     */
    public DeliveryOrder getCurrentSelectedOrder() {

        return currentSelectedOrder;
    }

    public void setCurrentSelectedOrder(DeliveryOrder currentSelectedOrder) {

        this.currentSelectedOrder = currentSelectedOrder;
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
