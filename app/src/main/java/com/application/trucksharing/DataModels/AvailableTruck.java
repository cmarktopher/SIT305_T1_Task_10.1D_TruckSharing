package com.application.trucksharing.DataModels;

import android.media.Image;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data model for available trucks
 * By the looks of it, we need to store an image of the truck based on the task sheet indicating an image in the wireframe
 * According to this post https://stackoverflow.com/questions/46337519/how-insert-image-in-room-persistence-library
 * it seems storing an image into a database is not a good idea
 * So, I think the way I'll handle this is store some kind of url or path to an image.
 * For a local database, perhaps I'll store a path to a local storage of truck images.
 * If this ever gets moved from this local database to a database over the net such as firebase,
 * I think we would hold a ref/link to another server containing our images (just an assumption, haven't research this yet.).
 */

@Entity(tableName = "available_trucks")
public class AvailableTruck {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "full_name")
    public String truckName;

    @ColumnInfo(name = "truck_description")
    public String truckDescription;

    @ColumnInfo(name = "image_link")
    public String imageLink;

    public AvailableTruck(String truckName, String imageLink) {
        this.truckName = truckName;
        this.imageLink = imageLink;
    }
}
