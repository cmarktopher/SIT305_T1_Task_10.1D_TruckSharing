package com.application.trucksharing.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.transition.MaterialFadeThrough;

import java.util.ArrayList;
import java.util.List;

/**
 * Home fragment that will show all available trucks in the database.
 */
public class HomeFragment extends Fragment {

    // UI element binding
    FragmentHomeBinding binding;

    // Nav menu window
    PopupWindow navPopUpWindow;

    private final List<AvailableTruck> availableTrucks = new ArrayList<>();

    public HomeFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        handleFragmentTransitions();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        // Create our navigation menu view
        createNavigationMenuPopUpWindow();

        // Bind to add new delivery button
        binding.homeFragmentNewDeliveryButton.setOnClickListener(this::onNewDeliveryButtonPressed);

        // Bind to show menu button
        binding.homeFragmentShowHomeMenuButton.setOnClickListener(this::onShowMenuButtonPressed);

        return view;
    }

    /**
     * Creates a pop up window for the navigation menu.
     * Will bind menu buttons here too.
     */
    private void createNavigationMenuPopUpWindow(){

        // Create our nav menu view
        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        @SuppressLint("InflateParams") View navMenuView = inflater.inflate(R.layout.view_navigation_menu, null);

        // Create the pop up window
        navPopUpWindow = new PopupWindow(
                navMenuView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        navPopUpWindow.setFocusable(true);
        navPopUpWindow.setOutsideTouchable(true);

        // Do the button bindings - Will leave account out since we won't need it for the task.
        MaterialButton menuButton = navMenuView.findViewById(R.id.navigationMenuViewHomeButton);
        MaterialButton myOrdersButton = navMenuView.findViewById(R.id.navigationMenuViewOrdersButton);

        menuButton.setOnClickListener(menuButtonView -> {

            // For this button, we don't need to navigate anywhere since we are already at the main menu
            navPopUpWindow.dismiss();
        });

        myOrdersButton.setOnClickListener(myOrdersButtonView -> {

            NavDirections action = HomeFragmentDirections.actionHomeFragmentToMyOrdersFragment();
            Navigation.findNavController(getView()).navigate(action);
            navPopUpWindow.dismiss();
        });
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
     * I wanted to try something new, and use a pop up menu.
     * @param view View pressed.
     */
    private void onShowMenuButtonPressed(View view){

        // Show at the top left of the window.
        navPopUpWindow.showAtLocation(view, Gravity.TOP|Gravity.START, 0, 0);
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