package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.application.trucksharing.R;
import com.application.trucksharing.databinding.FragmentHomeMenuBinding;
import com.google.android.material.transition.MaterialFade;
import com.google.android.material.transition.MaterialFadeThrough;

/**
 * This fragment is for the navigation home menu that will overlay over the home and my orders fragment.
 */
public class NavigationMenuFragment extends Fragment {

    public NavigationMenuFragment() {

        // Required empty public constructor
    }

    public static NavigationMenuFragment newInstance() {
        NavigationMenuFragment fragment = new NavigationMenuFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentHomeMenuBinding binding = FragmentHomeMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to home button
        binding.homeMenuHomeButton.setOnClickListener(homeButtonView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .remove(this)
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .replace(R.id.coreFragmentContainerView, HomeFragment.newInstance(), null)
                    .commit();
        });

        // Bind the account button - doesn't seem to be required for this task so I will leave it as a to do
        // TODO Add in account fragment

        // Bind to orders button
        binding.homeMenuOrdersButton.setOnClickListener(homeButtonView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .remove(this)
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .replace(R.id.coreFragmentContainerView, MyOrdersFragment.newInstance(), null)
                    .commit();
        });

        // I wanted to add a way to drop the menu if we click outside of it
        binding.navigationMenuFrameView.setOnClickListener(navigationMenuView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .remove(this)
                    .commit();
        });

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