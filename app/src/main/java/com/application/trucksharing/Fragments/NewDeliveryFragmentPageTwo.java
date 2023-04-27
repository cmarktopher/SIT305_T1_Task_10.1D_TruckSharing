package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentNewDeliveryPageOneBinding;
import com.application.trucksharing.databinding.FragmentNewDeliveryPageTwoBinding;

/**
 * Page 2 for the new delivery fragment
 */
public class NewDeliveryFragmentPageTwo extends Fragment {

    public NewDeliveryFragmentPageTwo() {


    }

    public static NewDeliveryFragmentPageTwo newInstance() {

        NewDeliveryFragmentPageTwo fragment = new NewDeliveryFragmentPageTwo();
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

        // Create binding and view
        FragmentNewDeliveryPageTwoBinding binding = FragmentNewDeliveryPageTwoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Now, since this is page two of the form, lets populate the rest of the delivery order object we already have once we press the submit button
        binding.createOrderButton.setOnClickListener(createOrderView -> {

            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
            DeliveryOrder deliveryOrder = deliveryOrderViewModel.getPendingNewDeliveryOrder();

            // Since we are using the radio group, this is the way we can get the text out from the selected radio button
            // However, since the id is based on what was selected, we need to do a null check in case we didn't select anything
            int goodTypeRadioID = binding.goodTypesRadioGroup.getCheckedRadioButtonId();
            RadioButton goodTypeRadioButton = view.findViewById(goodTypeRadioID);

            if (goodTypeRadioButton != null){

                // As an extra piece of logic, since I've set up a text based input for other, we need to cater for the other radio button options differently
                if (goodTypeRadioButton == binding.newGoodsOtherRadioButton) {

                    deliveryOrder.goodType = binding.goodTypeOtherInput.getText().toString();
                }
                else{

                    deliveryOrder.goodType = goodTypeRadioButton.getText().toString();
                }
            }

            // Weight and dimensions
            deliveryOrder.weight = binding.newDeliveryWeightInput.getText().toString();
            deliveryOrder.width = binding.newDeliveryWidthInput.getText().toString();
            deliveryOrder.length = binding.newDeliveryLengthInput.getText().toString();
            deliveryOrder.height = binding.newDeliveryHeightInput.getText().toString();

            // Handle vehicle type radio group same as good type
            int vehicleTypeRadioID = binding.vehicleTypeRadioGroup.getCheckedRadioButtonId();
            RadioButton vehicleTypeRadioButton = view.findViewById(vehicleTypeRadioID);

            if (vehicleTypeRadioButton != null){

                // As an extra piece of logic, since I've set up a text based input for other, we need to cater for the other radio button options differently
                if (vehicleTypeRadioButton == binding.vehicleTypeOtherRadioButon) {

                    deliveryOrder.vehicleType = binding.vehicleTypeOtherInput.getText().toString();
                }
                else{

                    deliveryOrder.vehicleType = vehicleTypeRadioButton.getText().toString();
                }
            }

            deliveryOrderViewModel.insertNewAvailableTruck();

            // Do the transition
            FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .replace(R.id.coreFragmentContainer, HomeFragment.newInstance(), null)
                    .commit();
        });


        return view;
    }
}