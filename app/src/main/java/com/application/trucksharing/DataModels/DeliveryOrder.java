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

    @ColumnInfo(name = "pickup_latitude")
    public double pickupLatitude;

    @ColumnInfo(name = "pickup_longitude")
    public double pickupLongitude;

    @ColumnInfo(name = "drop_off_location")
    public String dropOffLocation;

    @ColumnInfo(name = "drop_off_latitude")
    public double dropOffLatitude;

    @ColumnInfo(name = "drop_off_longitude")
    public double dropOffLongitude;

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

    public DeliveryOrder(){}

    public DeliveryOrder(int uid, String senderName, String receiverName,
                         String pickupDate, String pickupTime,
                         String pickupLocation, double pickupLatitude, double pickupLongitude,
                         String dropOffLocation, double dropOffLatitude, double dropOffLongitude,
                         String goodType, String weight, String width, String length, String height, String vehicleType) {
        this.uid = uid;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.dropOffLocation = dropOffLocation;
        this.dropOffLatitude = dropOffLatitude;
        this.dropOffLongitude = dropOffLongitude;
        this.goodType = goodType;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.vehicleType = vehicleType;
    }
}
