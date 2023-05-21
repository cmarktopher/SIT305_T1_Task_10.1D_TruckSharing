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
import com.application.trucksharing.R;
import com.application.trucksharing.RecyclerViews.MyOrdersAdapter;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentMyOrdersBinding;

/**
 * Fragment to show all current orders
 */
public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {


    }

    public static MyOrdersFragment newInstance() {

        MyOrdersFragment fragment = new MyOrdersFragment();
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
        FragmentMyOrdersBinding binding = FragmentMyOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to show menu button
        ImageButton showMenuButton = view.findViewById(R.id.showHomeMenuButton);
        showMenuButton.setOnClickListener(showHomeMenuView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .add(R.id.coreFragmentContainerView, NavigationMenuFragment.newInstance(), null)
                    .commit();
        });

        // Create and bind recycler view adapter to the recycler view
        MyOrdersAdapter newOrdersAdapter = new MyOrdersAdapter(requireActivity());
        binding.ordersRecyclerView.setAdapter(newOrdersAdapter);
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        // Handle data
        DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
        deliveryOrderViewModel.getAllDeliveryOrders().observe(getViewLifecycleOwner(), items -> {

            newOrdersAdapter.UpdateOrder(items);
        });

        return view;
    }
}