package com.application.trucksharing.DataModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data model for delivery orders
 */
@Entity(tableName = "delivery_order")
public class DeliveryOrder {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "sender_name")
    public String senderName;

    @ColumnInfo(name = "receiver_name")
    public String receiverName;

    @ColumnInfo(name = "pickup_date")
    public String pickupDate;

    @ColumnInfo(name = "pickup_time")
    public String pickupTime;

    @ColumnInfo(name = "pickup_location")
    public String pickupLocation;

    @ColumnInfo(name = "good_type")
    public String goodType;

    @ColumnInfo(name = "weight")
    public String weight;

    @ColumnInfo(name = "width")
    public String width;

    @ColumnInfo(name = "length")
    public String length;

    @ColumnInfo(name = "height")
    public String height;

    @ColumnInfo(name = "vehicle_type")
    public String vehicleType;

    public DeliveryOrder(){};

    public DeliveryOrder(int uid, String receiverName, String pickupDate, String pickupTime, String pickupLocation, String goodType, String weight, String width, String length, String height, String vehicleType) {
        this.uid = uid;
        this.receiverName = receiverName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.goodType = goodType;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.vehicleType = vehicleType;
    }
}
