package com.application.trucksharing.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.application.trucksharing.DataModels.User;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.UserViewModel;
import com.application.trucksharing.databinding.FragmentLogInBinding;

/**
 * Fragment for the log in page - this will also be the entry view the user sees when opening the app
 */
public class LogInFragment extends Fragment {

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance() {

        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentLogInBinding binding = FragmentLogInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to log in button
        binding.loginButton.setOnClickListener(nClickView -> {

            String userName = binding.logInUserNameInputView.getText().toString();
            String password = binding.logInPasswordInputView.getText().toString();

            // Get the user and compare the password - will need a better way to handle authentication I reckon...
            UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            User user = userViewModel.getUserByUserName(userName);

            if (user != null){

                // TODO Perform proper authentication - need to decode the password in the database and do the comparison

                if (user.passWord.equals(password)){

                    Log.d("Authentication", "Password Match");
                    return;
                }

                Log.d("Authentication", "Password Don't Match");
            }
            else{

                Log.d("Authentication", "User not found");
            }
        });

        // Bind to sign up button so that we can transition to sign up fragment which has the sign up form
        binding.signUpButton.setOnClickListener(onClickView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .replace(R.id.coreFragmentContainer, SignUpFragment.newInstance(), null)
                    .commit();
        });

        return view;
    }
}