package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentNewDeliveryPageOneBinding;
import java.text.SimpleDateFormat;

/**
 * Fragment for the first page for a new delivery order.
 * Just a note, since the form has been split into two fragments based on the task sheet,
 * I'll use the view model associated with new deliveries to persist the data through the different fragments.
 * This will allow me to keep the data from page 1 of the new delivery.
 */
public class NewDeliveryFragmentPageOne extends Fragment {


    public NewDeliveryFragmentPageOne() {

        // Required empty public constructor
    }

    public static NewDeliveryFragmentPageOne newInstance() {
        NewDeliveryFragmentPageOne fragment = new NewDeliveryFragmentPageOne();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        FragmentNewDeliveryPageOneBinding binding = FragmentNewDeliveryPageOneBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to next button
        binding.newDeliveryNextButton.setOnClickListener(nextButtonView -> {

            // Before we transition to the next fragment, lets populate the pending new delivery object in the view model
            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
            DeliveryOrder deliveryOrder = deliveryOrderViewModel.getPendingNewDeliveryOrder();

            deliveryOrder.receiverName = binding.newDeliveryReceiverNameInput.getText().toString();

            SimpleDateFormat dateFormatObject = new SimpleDateFormat("dd-MM-yyyy");
            deliveryOrder.pickupDate = String.valueOf(dateFormatObject.format(binding.newDeliveryCalenderView.getDate()));

            deliveryOrder.pickupTime = binding.newDeliveryTimeInput.getText().toString();
            deliveryOrder.pickupLocation = binding.newDeliveryLocationInput.getText().toString();

            // Do the transition
            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.coreFragmentContainer, NewDeliveryFragmentPageTwo.newInstance(), null)
                    .commit();
        });

        // Inflate the layout for this fragment
        return view;
    }
}