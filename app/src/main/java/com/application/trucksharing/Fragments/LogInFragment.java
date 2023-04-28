package com.application.trucksharing.Fragments;

import android.os.Bundle;
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

        return new LogInFragment();
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

            handleLogIn(binding);
        });

        // Bind to sign up button so that we can transition to sign up fragment which has the sign up form
        binding.signUpButton.setOnClickListener(onClickView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.coreFragmentContainer, SignUpFragment.newInstance(), null)
                    .commit();
        });

        return view;
    }

    /**
     * This method will be quite simple and just handle transition if credentials are correct.
     * I'll just note that I didn't spend much time looking into proper authentication and authorization techniques at this point.
     * For now, I'll keep it simple.
     */
    private void handleLogIn(FragmentLogInBinding binding){

        String userName = binding.logInUserNameInputView.getText().toString();
        String password = binding.logInPasswordInputView.getText().toString();

        // First check the user name
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User user = userViewModel.getUserByUserName(userName);

        if (user != null){

            if (user.passWord.equals(password)){

                FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.coreFragmentContainer, HomeFragment.newInstance(), null)
                        .commit();

                return;
            }

            binding.logInPasswordInputLayout.setError("Password is Incorrect");
        }
        else{

            binding.logInUserNameInputLayout.setError("User not Found");
        }
    }
}