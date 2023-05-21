package com.application.trucksharing.Authentication;

import androidx.fragment.app.FragmentActivity;

public interface IAuthenticationHandler {

    Boolean authenticateUser(FragmentActivity activity, String username, String password);
}
