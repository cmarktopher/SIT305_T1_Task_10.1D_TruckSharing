package com.application.trucksharing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.application.trucksharing.R;
import com.application.trucksharing.databinding.FragmentHomeMenuBinding;
import com.google.android.material.transition.MaterialFade;

/**
 * THIS IS NO LONGER IN USE.
 * I've swapped this with a simple pop up window which works better with navigation graph.
 */
public class NavigationMenuFragment extends Fragment {

    public NavigationMenuFragment() {

        // Required empty public constructor
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
        FragmentHomeMenuBinding binding = FragmentHomeMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to home button
        binding.homeMenuHomeButton.setOnClickListener(homeButtonView -> Navigation.findNavController(view).popBackStack(R.id.homeFragment, false));

        // Bind the account button - doesn't seem to be required for this task so I will leave it as a to do
        // TODO Add in account fragment

        // Bind to orders button
        binding.homeMenuOrdersButton.setOnClickListener(homeButtonView -> {

            //NavDirections action = NavigationMenuFragmentDirections.actionNavigationMenuFragmentToMyOrdersFragment();
            //Navigation.findNavController(view).navigate(action);
        });

        // I wanted to add a way to drop the menu if we click outside of it
        binding.navigationMenuFrameView.setOnClickListener(navigationMenuView -> Navigation.findNavController(view).popBackStack(R.id.homeFragment, false));

        return view;
    }

    private void handleFragmentTransitions(){

        MaterialFade enterFade = new MaterialFade();
        enterFade.setDuration(getResources().getInteger(R.integer.fade_enter_duration));

        MaterialFade exitFade = new MaterialFade();
        exitFade.setDuration(getResources().getInteger(R.integer.fade_exit_duration));
        exitFade.setInterpolator(new LinearInterpolator());

        setEnterTransition(enterFade);
        setExitTransition(exitFade);
    }
}