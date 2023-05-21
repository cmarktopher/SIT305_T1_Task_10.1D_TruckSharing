package com.application.trucksharing.Authentication;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.application.trucksharing.DataModels.User;
import com.application.trucksharing.ViewModels.UserViewModel;

public class RoomAuthenticationHandler implements IAuthenticationHandler{

    @Override
    public Boolean authenticateUser(FragmentActivity activity, String username, String password) {

        UserViewModel userViewModel = new ViewModelProvider(activity).get(UserViewModel.class);

        // Get a user from the view model via the username
        User user = userViewModel.getUserByUserName(username);

        // Check if we managed to get a user and then check if password matches.
        if (user != null && user.passWord.matches(password)){

            return true;
        }

        // If we reach here, then we need to fail the authentication.
        return false;
    }
}
