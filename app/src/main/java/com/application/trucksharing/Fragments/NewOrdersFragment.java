package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.RecyclerViews.AvailableTrucksAdapter;
import com.application.trucksharing.RecyclerViews.NewOrdersAdapter;
import com.application.trucksharing.ViewModels.AvailableTruckViewModel;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentHomeMenuBinding;
import com.application.trucksharing.databinding.FragmentNewOrdersBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to show all current orders
 */
public class NewOrdersFragment extends Fragment {

    private List<DeliveryOrder> newOrders = new ArrayList<>();

    public NewOrdersFragment() {


    }

    // TODO: Rename and change types and number of parameters
    public static NewOrdersFragment newInstance() {

        NewOrdersFragment fragment = new NewOrdersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentNewOrdersBinding binding = FragmentNewOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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

        // Create and bind recycler view adapter to the recycler view
        NewOrdersAdapter newOrdersAdapter = new NewOrdersAdapter(requireActivity(), newOrders);
        binding.ordersRecyclerView.setAdapter(newOrdersAdapter);
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        // Handle data
        DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
        deliveryOrderViewModel.getAllDeliveryOrders().observe(getViewLifecycleOwner(), items -> {

            newOrders.clear();
            newOrders.addAll(items);
            newOrdersAdapter.notifyDataSetChanged();
        });

        return view;
    }
}