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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.application.trucksharing.R;
import com.application.trucksharing.RecyclerViews.MyOrdersAdapter;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentMyOrdersBinding;
import com.google.android.material.button.MaterialButton;

/**
 * Fragment to show all current orders
 */
public class MyOrdersFragment extends Fragment {

    // Nav menu window
    PopupWindow navPopUpWindow;

    public MyOrdersFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentMyOrdersBinding binding = FragmentMyOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Create our navigation menu view
        createNavigationMenuPopUpWindow();

        // Bind to show menu button
        binding.myOrdersFragmentShowMenuButton.setOnClickListener(this::onShowMenuButtonPressed);

        // Create and bind recycler view adapter to the recycler view
        MyOrdersAdapter newOrdersAdapter = new MyOrdersAdapter(requireActivity());
        binding.ordersRecyclerView.setAdapter(newOrdersAdapter);
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        // Handle data
        DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
        deliveryOrderViewModel.getAllDeliveryOrders().observe(getViewLifecycleOwner(), newOrdersAdapter::UpdateOrder);

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

            // We just need to pop off the fragment
            Navigation.findNavController(getView()).popBackStack();
            navPopUpWindow.dismiss();
        });

        myOrdersButton.setOnClickListener(myOrdersButtonView -> navPopUpWindow.dismiss());
    }

    /**
     * Response to show menu button being pressed.
     * @param view View pressed.
     */
    private void onShowMenuButtonPressed(View view){

        // Show at the top left of the window.
        navPopUpWindow.showAtLocation(view, Gravity.TOP|Gravity.START, 0, 0);
    }
}