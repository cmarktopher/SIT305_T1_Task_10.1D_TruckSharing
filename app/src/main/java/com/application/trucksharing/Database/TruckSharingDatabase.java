package com.application.trucksharing.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.application.trucksharing.DataAccessObjects.UsersDAO;
import com.application.trucksharing.DataModels.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Database for the entire truck sharing up.
 */
@Database(entities = {User.class}, version = 1)
public abstract class TruckSharingDatabase extends RoomDatabase {

        // Keep track of the single instance of this database
        private static volatile TruckSharingDatabase INSTANCE;

        // Our DAO containing our queries
        public abstract UsersDAO usersDao();

        // Executor service that will be needed for queries to run on a background thread
        private final int NUMBER_OF_THREADS = 4;
        public final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        // Implementation of the singleton pattern
        public static TruckSharingDatabase getDatabase(final Context context) {

            if (INSTANCE == null){
                synchronized (TruckSharingDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TruckSharingDatabase.class, "truck_sharing_database").build();
                    }
                }
            }

            return INSTANCE;
        }
}
