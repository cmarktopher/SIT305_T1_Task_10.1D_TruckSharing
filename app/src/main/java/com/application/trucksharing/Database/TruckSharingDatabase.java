package com.application.trucksharing.Database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.application.trucksharing.DataAccessObjects.AvailableTruckDAO;
import com.application.trucksharing.DataAccessObjects.DeliveryOrderDAO;
import com.application.trucksharing.DataAccessObjects.UsersDAO;
import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.DataModels.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Database for the entire truck sharing up.
 */
@Database(entities = {User.class, AvailableTruck.class, DeliveryOrder.class}, version = 5)
public abstract class TruckSharingDatabase extends RoomDatabase {

        // Keep track of the single instance of this database
        private static volatile TruckSharingDatabase INSTANCE;

        // Our DAO containing our queries
        public abstract UsersDAO usersDao();
        public abstract AvailableTruckDAO availableTruckDAO();
        public abstract DeliveryOrderDAO deliveryOrderDAO();

        // Executor service that will be needed for queries to run on a background thread
        private static final int NUMBER_OF_THREADS = 4;
        public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        // Implementation of the singleton pattern
        public static TruckSharingDatabase getDatabase(final Context context) {

            if (INSTANCE == null){
                synchronized (TruckSharingDatabase.class) {
                    if (INSTANCE == null) {

                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TruckSharingDatabase.class, "truck_sharing_database").addCallback(roomDatabaseCallback).build();
                    }
                }
            }

            return INSTANCE;
        }

        // So, in terms of the available trucks, in a real application, we would likely
        // have a database on an external server that gets updated regularly on availability of trucks once they have
        // finished their delivery or if we get more trucks added into the system.
        // Also, truck drivers or other admin role users would have a separate interface in the app itself based on login
        // authorization or possibly, a separate app that will handle interaction with this database.
        // This app, authorized with regular users, would simply keep track of this information from that database.
        // Since we do not have an external database or a separate system that handles authorization (outside scope of task),
        // we will have to initialize the database with available trucks.
        // Following this guide: https://developer.android.com/codelabs/android-room-with-a-view#12
        // we can use the onCreate callback to initialize the database with data
        private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db){

                super.onCreate(db);

                Log.d("Test", "Created");

                databaseWriteExecutor.execute(() -> {

                    // Will just delete all entries from here
                    AvailableTruckDAO availableTruckDAO = INSTANCE.availableTruckDAO();
                    availableTruckDAO.deleteAll();

                    // Add some available trucks

                    availableTruckDAO.insertNewAvailableTruck(new AvailableTruck(
                        "Truck_01",
                        "A large truck. Cheap!",
                            ""
                    ));

                    availableTruckDAO.insertNewAvailableTruck(new AvailableTruck(
                            "Truck_02",
                            "An even larger truck.",
                            ""
                    ));

                    availableTruckDAO.insertNewAvailableTruck(new AvailableTruck(
                            "Truck_03",
                            "A truck with refrigeration.",
                            ""
                    ));

                    availableTruckDAO.insertNewAvailableTruck(new AvailableTruck(
                            "Truck_04",
                            "A small truck for a small number of items. Guaranteed quick deliver.",
                            ""
                    ));

                });

            }
        };


}
