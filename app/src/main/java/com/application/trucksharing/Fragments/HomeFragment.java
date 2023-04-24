package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.R;
import com.application.trucksharing.RecyclerViews.AvailableTrucksAdapter;
import com.application.trucksharing.ViewModels.AvailableTruckViewModel;
import com.application.trucksharing.databinding.FragmentHomeBinding;
import com.application.trucksharing.databinding.FragmentLogInBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Home fragment that will show all available trucks in the database.
 * This feature is according to the task sheet.
 * Personally, I would have had the home page show my orders and have available trucks be shown when making an order.
 * Or, perhaps not even show available trucks at all and let that be handled internally and have availability based on requirements be communicated with the user (ie. do we have a truck available that meets the requirements of the user).
 * Regardless, I will just follow the wireframes and requirements (just putting down some of my thoughts).
 */
public class HomeFragment extends Fragment {

    private List<AvailableTruck> availableTrucks = new ArrayList<>();

    public HomeFragment() {

        // Required empty public constructor
    }

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get UI Elements
        RecyclerView availableTrucksRecyclerView = view.findViewById(R.id.availableTrucksRecyclerView);

        // Create and bind recycler view adapter to the recycler view
        AvailableTrucksAdapter availableTrucksAdapter = new AvailableTrucksAdapter(requireActivity(), availableTrucks);
        availableTrucksRecyclerView.setAdapter(availableTrucksAdapter);
        availableTrucksRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        // Handle data
        AvailableTruckViewModel availableTruckViewModel = new ViewModelProvider(requireActivity()).get(AvailableTruckViewModel.class);
        availableTruckViewModel.getAllAvailableTrucks().observe(getViewLifecycleOwner(), items -> {

            availableTrucks.clear();
            availableTrucks.addAll(items);
            availableTrucksAdapter.notifyDataSetChanged();
        });

        // Bind to add new delivery button
        FloatingActionButton newDeliveryButton = view.findViewById(R.id.newDeliveryButton);
        newDeliveryButton.setOnClickListener(newDeliveryButtonView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.coreFragmentContainer, NewDeliveryFragmentPageOne.newInstance(), null)
                    .commit();
        });

        return view;
    }
}