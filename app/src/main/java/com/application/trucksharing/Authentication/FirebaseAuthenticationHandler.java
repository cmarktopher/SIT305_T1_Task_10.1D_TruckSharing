package com.application.trucksharing.Authentication;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class FirebaseAuthenticationHandler implements IAuthenticationHandler {

    @Override
    public Boolean authenticateUser(FragmentActivity activity, String username, String password) {

        return false;
    }
}
