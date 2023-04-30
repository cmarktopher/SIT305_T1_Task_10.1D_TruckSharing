package com.application.trucksharing.Fragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.R;
import com.application.trucksharing.RecyclerViews.AvailableTrucksAdapter;
import com.application.trucksharing.ViewModels.AvailableTruckViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.MaterialFadeThrough;

import java.util.ArrayList;
import java.util.List;

/**
 * Home fragment that will show all available trucks in the database.
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
        handleFragmentTransitions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get UI Elements
        RecyclerView availableTrucksRecyclerView = view.findViewById(R.id.ordersRecyclerView);

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
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.coreFragmentContainer, NewDeliveryFragmentPageOne.newInstance(), null)
                    .commit();
        });

        // Bind to show menu button
        ImageButton showMenuButton = view.findViewById(R.id.showHomeMenuButton);
        showMenuButton.setOnClickListener(showHomeMenuView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .add(R.id.coreFragmentContainer, NavigationMenuFragment.newInstance(), null)
                    .commit();
        });

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