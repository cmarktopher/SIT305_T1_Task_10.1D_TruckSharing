package com.application.trucksharing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.lifecycle.ViewModelProvider;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentNewDeliveryPageOneBinding;
import com.google.android.material.transition.MaterialSharedAxis;

import java.text.SimpleDateFormat;

/**
 * Fragment for the first page for a new delivery order.
 * Just a note, since the form has been split into two fragments based on the task sheet,
 * I'll use the view model associated with new deliveries to persist the data through the different fragments.
 * This will allow me to keep the data from page 1 of the new delivery.
 * If there is any confusion as to why
 */
public class NewDeliveryFragmentPageOne extends Fragment {


    public NewDeliveryFragmentPageOne() {

        // Required empty public constructor
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
        FragmentNewDeliveryPageOneBinding binding = FragmentNewDeliveryPageOneBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to next button
        binding.newDeliveryNextButton.setOnClickListener(nextButtonView -> {

            // Before we transition to the next fragment, lets populate the pending new delivery object in the view model
            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
            DeliveryOrder deliveryOrder = deliveryOrderViewModel.getPendingNewDeliveryOrder();

            deliveryOrder.receiverName = binding.newDeliveryReceiverNameInput.getText().toString();
            deliveryOrder.senderName = binding.newDeliverySenderNameInput.getText().toString(); // Just something extra I added since it seems we need one based on the details page of an order

            SimpleDateFormat dateFormatObject = new SimpleDateFormat("dd-MM-yyyy");
            deliveryOrder.pickupDate = String.valueOf(dateFormatObject.format(binding.newDeliveryCalenderView.getDate()));

            deliveryOrder.pickupTime = binding.newDeliveryTimeInput.getText().toString();
            deliveryOrder.pickupLocation = binding.newDeliveryPickUpLocationInput.getText().toString();

            // Added this in after everything else was done - a bit lazy approach since I didn't want to put error messages for each input like the sign in and sign up
            if (deliveryOrder.receiverName.isEmpty() || deliveryOrder.senderName.isEmpty()|| deliveryOrder.pickupDate.isEmpty() || deliveryOrder.pickupTime.isEmpty() || deliveryOrder.pickupLocation.isEmpty() ){

                Toast toast = new Toast(requireContext());
                toast.setText("Please fill in all fields");
                toast.show();

                return;
            }

            // Do the transition
            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.coreFragmentContainerView, NewDeliveryFragmentPageTwo.newInstance(), null)
                    .commit();
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * A new material motion pattern known as shared axis where we move the views on the X-axis in a single direction
     */
    private void handleFragmentTransitions(){

        MaterialSharedAxis exitAxisTransition = new MaterialSharedAxis(MaterialSharedAxis.X, true);
        exitAxisTransition.setInterpolator(new FastOutLinearInInterpolator());
        exitAxisTransition.setDuration(getResources().getInteger(R.integer.shared_axis_exit_duration));

        MaterialSharedAxis reEnterAxisTransition = new MaterialSharedAxis(MaterialSharedAxis.X, false);
        reEnterAxisTransition.setDuration(getResources().getInteger(R.integer.shared_axis_enter_duration));
        reEnterAxisTransition.setInterpolator(new FastOutLinearInInterpolator());

        setExitTransition(exitAxisTransition);
        setReenterTransition(reEnterAxisTransition);
    }


}