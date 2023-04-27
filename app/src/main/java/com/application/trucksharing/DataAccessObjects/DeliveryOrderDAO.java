package com.application.trucksharing.DataAccessObjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.application.trucksharing.DataModels.DeliveryOrder;

import java.util.List;

@Dao
public interface DeliveryOrderDAO {

    @Query("SELECT * FROM delivery_order")
    LiveData<List<DeliveryOrder>> getAllDeliveryOrders();

    @Insert
    void insertNewDeliveryOrder(DeliveryOrder newDeliveryOrder);
}
