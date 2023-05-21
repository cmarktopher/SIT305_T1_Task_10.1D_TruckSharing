package com.application.trucksharing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.R;
import com.application.trucksharing.RecyclerViews.AvailableTrucksAdapter;
import com.application.trucksharing.ViewModels.AvailableTruckViewModel;
import com.application.trucksharing.databinding.FragmentHomeBinding;
import com.google.android.material.transition.MaterialFadeThrough;
import java.util.ArrayList;
import java.util.List;

/**
 * Home fragment that will show all available trucks in the database.
 */
public class HomeFragment extends Fragment {

    // UI element binding
    FragmentHomeBinding binding;

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

        // Create our binding and view
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get UI Elements
        RecyclerView availableTrucksRecyclerView = binding.homeFragmentOrdersRecyclerView;

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
        binding.homeFragmentNewDeliveryButton.setOnClickListener(this::onNewDeliveryButtonPressed);

        // Bind to show menu button
        binding.homeFragmentShowHomeMenuButton.setOnClickListener(this::onShowMenuButtonPressed);

        return view;
    }

    /**
     * Response to when new delivery button pressed.
     * @param view View pressed.
     */
    private void onNewDeliveryButtonPressed(View view){

        NavDirections action = HomeFragmentDirections.actionHomeFragmentToNewDeliveryFragmentPageOne();
        Navigation.findNavController(view).navigate(action);
    }

    /**
     * Response to when menu button pressed.
     * @param view View pressed.
     */
    private void onShowMenuButtonPressed(View view){

        NavDirections action = HomeFragmentDirections.actionHomeFragmentToNavigationMenuFragment();
        Navigation.findNavController(view).navigate(action);
    }

    /**
     * Handle transition settings.
     */
    private void handleFragmentTransitions(){

        MaterialFadeThrough enterFadeThrough = new MaterialFadeThrough();
        enterFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_enter_duration));

        MaterialFadeThrough exitFadeThrough = new MaterialFadeThrough();
        exitFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_exit_duration));

        setEnterTransition(enterFadeThrough);
        setExitTransition(exitFadeThrough);
    }
}