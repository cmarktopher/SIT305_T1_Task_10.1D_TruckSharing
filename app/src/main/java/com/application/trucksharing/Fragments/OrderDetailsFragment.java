package com.application.trucksharing.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.RecyclerViews.GridCardTextDisplayAdapter;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentOrderDetailsBinding;

import java.util.ArrayList;

/**
 * Fragment for displaying our detailed order/delivery
 * I've realized that doing a grid based layout is really painful manually and will be more painful if I ever decide to add more descriptions to the grid container
 * So, I decided to try a different approach using recycler view and adding the good description cards text procedurally.
 */
public class OrderDetailsFragment extends Fragment {

    // Our hashmap representing a goods description key and the corresponding text.
    private final ArrayList<String> goodsDescriptions;

    public OrderDetailsFragment() {

        // Initialize our good descriptions with some placeholder values
        goodsDescriptions = new ArrayList<>();
        goodsDescriptions.add("Goods Type");
        goodsDescriptions.add("Vehicle Type");
        goodsDescriptions.add("Weight");
        goodsDescriptions.add("Width");
        goodsDescriptions.add( "Length");
        goodsDescriptions.add("Height");
    }

    public static OrderDetailsFragment newInstance() {

        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentOrderDetailsBinding binding = FragmentOrderDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get order
        DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
        DeliveryOrder order = deliveryOrderViewModel.getCurrentSelectedOrder();

        // Update the sender/receiver name and time
        binding.fromSenderTextView.setText("From sender: "+ order.senderName);
        binding.pickUpTimeTextView.setText("Pick up time: " + order.pickupTime);
        binding.toReceiverTextView.setText("To receiver " + order.receiverName);

        // Just a note, a drop off time in the current context of the task is a bit odd
        // Not enough information has been provided about this
        // So for now, I'll assume drop off time is a live time that is updated on the truck driver's version of the app
        // We can achieve this by using live data most likely but since we don't have the full implementation of this app, I'll leave this as "to be determined", meaning that the truck driver needs to update this drop off time
        binding.dropOffTimeTextView.setText("Drop off time: to be determined");

        // Populate our data collection with goods descriptions respective to the order selection
        PopulateGoodsDescription(order);

        // Populate our order/delivery descriptions
        PopulateGoodsDescriptionAdapter(binding);

        // Bind to the get estimates button
        binding.orderDetailsFragmentGetEstimateButton.setOnClickListener(this::onGetEstimatePressed);

        return view;
    }

    /**
     * Populate the good description based on our data
     */
    private void PopulateGoodsDescription(DeliveryOrder order){

        // I realized while doing this that this will need some better handling - but since I know which index positions corresponds to what data, I'll leave it as is for now
        goodsDescriptions.set(0, "Goods Type: " + order.goodType);
        goodsDescriptions.set(1, "Vehicle Type: " + order.vehicleType);
        goodsDescriptions.set(2, order.weight);
        goodsDescriptions.set(3, order.width);
        goodsDescriptions.set(4, order.length);
        goodsDescriptions.set(5, order.height);
    }

    /**
     * Method to handle populating the recycler view with the item text display cards.
     */
    private void PopulateGoodsDescriptionAdapter(FragmentOrderDetailsBinding binding){

        GridCardTextDisplayAdapter newOrdersAdapter = new GridCardTextDisplayAdapter(requireActivity(), goodsDescriptions);
        binding.goodsDescriptionRecyclerView.setAdapter(newOrdersAdapter);

        // Since by default, we can scroll within this view, I would like to lock this feature.
        binding.goodsDescriptionRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2) {

            @Override
            public boolean canScrollVertically() {

                return false;
            }

            @Override
            public boolean canScrollHorizontally() {

                return false;
            }
        });
    }

    /**
     * Handle response to get estimate button being pressed.
     * @param view View being pressed.
     */
    void onGetEstimatePressed(View view){

        NavDirections action = OrderDetailsFragmentDirections.actionOrderDetailsFragmentToEstimateFragment();
        Navigation.findNavController(getView()).navigate(action);
    }
}