package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.application.trucksharing.DataModels.User;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.UserViewModel;
import com.application.trucksharing.databinding.FragmentSignUpBinding;
import com.google.android.material.transition.MaterialFadeThrough;

/**
 * Fragment for the sign up form
 */
public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        handleFragmentTransitions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentSignUpBinding binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to create account button
        binding.createAccountButton.setOnClickListener(view1 -> {

            String fullName = binding.signUpFullNameInputView.getText().toString();
            String userName = binding.signUpUserNameInputView.getText().toString();
            String passWord = binding.signUpPasswordInputView.getText().toString();
            String confirmPassWord = binding.signUpConfirmPasswordInputView.getText().toString();
            String number = binding.signUpPhoneInputView.getText().toString();

            // Make sure that password and confirm password matches
            // TODO If not, add in an error message.
            if (passWord.equals(confirmPassWord)){

                // Create a new user
                User newUser = new User(fullName, userName, passWord, number);

                // Update the database with our new user
                UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                userViewModel.insertNewUser(newUser);

                // Go back to home screen when done
                FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                        .setReorderingAllowed(true)
                        .replace(R.id.coreFragmentContainer, LogInFragment.newInstance(), null)
                        .commit();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void handleFragmentTransitions(){

        MaterialFadeThrough enterFadeThrough = new MaterialFadeThrough();
        enterFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_enter_duration));

        MaterialFadeThrough exitFadeThrough = new MaterialFadeThrough();
        exitFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_exit_duration));

        setEnterTransition(enterFadeThrough);
        setExitTransition(exitFadeThrough);
    }
}