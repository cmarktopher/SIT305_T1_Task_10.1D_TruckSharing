package com.application.trucksharing.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.application.trucksharing.DataModels.User;
import com.application.trucksharing.Repositories.UserRepository;

/**
 * View model for the user to bridge the UI to the repository.
 * At this point, I do wonder if we need this since we are not using live data.
 * Despite this, I'll include it anyway.
 */
public class UserViewModel extends AndroidViewModel {

    // Reference to user repository
    private UserRepository userRepository;

    /**
     * Initialize our user view model
     * @param application
     */
    public UserViewModel(@NonNull Application application) {

        super(application);

        userRepository = new UserRepository(application);
    }

    /**
     * Get a user by user name
     * @param userName
     * @return
     */
    public User getUserByUserName(String userName){

        return userRepository.getUserByUserName(userName);
    }

    /**
     *  Insert operation to create a new user entry for the database
     * @param user
     */
    public void insertNewUser(User user){

        userRepository.insertNewUser(user);
    }
}
