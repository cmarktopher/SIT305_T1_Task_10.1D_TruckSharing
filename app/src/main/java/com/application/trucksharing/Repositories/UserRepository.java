package com.application.trucksharing.Repositories;

import android.app.Application;
import android.util.Log;

import com.application.trucksharing.DataAccessObjects.UsersDAO;
import com.application.trucksharing.DataModels.User;
import com.application.trucksharing.Database.TruckSharingDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Repository to handle interaction with the user table in the database via the user data access object.
 */
public class UserRepository {

    // Cache the application ref
    private Application currentApplication;

    // User data access object
    private UsersDAO usersDAO;

    // Constructor to initialize our repository
    public UserRepository(Application application){

        currentApplication = application;
        usersDAO = TruckSharingDatabase.getDatabase(application).usersDao();
    }

    // Get a user by user name
    public User getUserByUserName(String userName){

        FutureTask<User> futureTask = new FutureTask<>(() -> usersDAO.getUserByUserName(userName));
        TruckSharingDatabase.getDatabase(currentApplication).databaseWriteExecutor.execute(futureTask);

        User user = null;

        try {

            user = futureTask.get();

        } catch (ExecutionException e) {

            throw new RuntimeException(e);

        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }

        return user;
    }

    // Insert operation to create a new user entry for the database
    public void insertNewUser(User user){

        // TODO Perform password hashing here

        // Insert the user to the database
        TruckSharingDatabase.databaseWriteExecutor.execute(() -> {
            usersDAO.insertNewUser(user);
        });
    }

}
