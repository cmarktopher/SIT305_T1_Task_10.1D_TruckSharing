package com.application.trucksharing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.application.trucksharing.Authentication.IAuthenticationHandler;
import com.application.trucksharing.Authentication.RoomAuthenticationHandler;
import com.application.trucksharing.R;
import com.application.trucksharing.databinding.FragmentLogInBinding;
import com.google.android.material.transition.MaterialFadeThrough;

/**
 * Fragment for the log in page.
 */
public class LogInFragment extends Fragment {

    // UI Binding
    FragmentLogInBinding binding;

    // Implementation of our authentication handler
    IAuthenticationHandler authenticationHandler = new RoomAuthenticationHandler();

    public LogInFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        handleFragmentTransitions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to log in button
        binding.loginButton.setOnClickListener(this::onLogInButtonPressed);

        // Bind to sign up button
        binding.signUpButton.setOnClickListener(this::onSignUpButtonPressed);

        return view;
    }

    /**
     * Just a simple method to apply Material Motion's Fade Through animation.
     */
    private void handleFragmentTransitions(){

        MaterialFadeThrough enterFadeThrough = new MaterialFadeThrough();
        enterFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_enter_duration));

        MaterialFadeThrough exitFadeThrough = new MaterialFadeThrough();
        exitFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_exit_duration));

        setEnterTransition(enterFadeThrough);
        setExitTransition(exitFadeThrough);
    }


    /**
     * Response to log in button being pressed.
     * @param view View pressed.
     */
    private void onLogInButtonPressed(View view){

        // Get our username and password
        String userName = binding.logInFragmentUserNameInputView.getText().toString();
        String password = binding.logInFragmentPasswordInputView.getText().toString();

        // Pass in our username and password to the authentication handler.
        if (authenticationHandler.authenticateUser(getActivity(), userName, password)){

            NavDirections action = LogInFragmentDirections.actionLogInFragmentToHomeFragment();
            Navigation.findNavController(view).navigate(action);

        }
        else{

            // If we reach here, something is wrong with our credentials.
            // For perhaps more security, we will just say that the username or password is incorrect for both fields.
            binding.logInFragmentUserNameInputLayout.setError("Username or password is incorrect");
            binding.logInFragmentPasswordInputLayout.setError("Username or password is incorrect");
        }
    }

    /**
     * Response to sign up button being pressed.
     * @param view View pressed.
     */
    private void onSignUpButtonPressed(View view){

        NavDirections action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment();
        Navigation.findNavController(view).navigate(action);
    }
}