package com.application.trucksharing.Fragments;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentNewDeliveryPageOneBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.transition.MaterialSharedAxis;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Fragment for the first page for a new delivery order.
 * Just a note, since the form has been split into two fragments based on the task sheet,
 * I'll use the view model associated with new deliveries to persist the data through the different fragments.
 * This will allow me to keep the data from page 1 of the new delivery.
 * If there is any confusion as to why
 */
public class NewDeliveryFragmentPageOne extends Fragment {

    // UI binding
    FragmentNewDeliveryPageOneBinding binding;

    // Reference to our AutocompleteSupportFragments for pick up and drop off
    AutocompleteSupportFragment pickUpAutocompleteSupportFragment;
    AutocompleteSupportFragment dropOffAutocompleteSupportFragment;

    // Variables to keep track of our places address, latitude and longitude.
    double pickUpLatitude;
    double pickUpLongitude;
    String pickUpAddress;

    double dropOffLatitude;
    double dropOffLongitude;
    String dropOffAddress;

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
        binding = FragmentNewDeliveryPageOneBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Locations auto complete setup
        initializePlaces();

        // Bind to next button
        binding.newDeliveryNextButton.setOnClickListener(this::onNextButtonViewPressed);

        return view;
    }

    /**
     * Initialize places.
     */
    private void initializePlaces(){

        try {
            ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);

            if (applicationInfo != null){

                String apiKey = applicationInfo.metaData.getString("com.google.android.geo.API_KEY");
                Places.initialize(getActivity().getApplicationContext(), apiKey);
            }


        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        // Setup our pick up location places input.
        pickUpAutocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.newDeliveryPickUpLocationInput);
        pickUpAutocompleteSupportFragment.setHint("Select Pick Up Location");
        pickUpAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG));

        pickUpAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

                Log.i("Error", "An error has occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                // Assign the latitude, longitude and address
                pickUpLatitude = place.getLatLng().latitude;
                pickUpLongitude = place.getLatLng().longitude;
                pickUpAddress = place.getAddress();

                pickUpAutocompleteSupportFragment.setHint(pickUpAddress);
            }
        });

        // Setup our drop off location places input.
        dropOffAutocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.newDeliveryDropOffLocationInput);
        dropOffAutocompleteSupportFragment.setHint("Select Drop Off Location");
        dropOffAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG));

        dropOffAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

                Log.i("Error", "An error has occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                // Assign the latitude, longitude and address
                dropOffLatitude = place.getLatLng().latitude;
                dropOffLongitude = place.getLatLng().longitude;
                dropOffAddress = place.getAddress();

                dropOffAutocompleteSupportFragment.setHint(dropOffAddress);
            }
        });
    }

    /**
     * Response to next button being pressed.
     * @param view View pressed.
     */
    private void onNextButtonViewPressed(View view){

        // Before we transition to the next fragment, lets populate the pending new delivery object in the view model
        DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
        DeliveryOrder deliveryOrder = deliveryOrderViewModel.getPendingNewDeliveryOrder();

        deliveryOrder.receiverName = binding.newDeliveryReceiverNameInput.getText().toString();
        deliveryOrder.senderName = binding.newDeliverySenderNameInput.getText().toString();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatObject = new SimpleDateFormat("dd-MM-yyyy");
        deliveryOrder.pickupDate = String.valueOf(dateFormatObject.format(binding.newDeliveryCalenderView.getDate()));

        deliveryOrder.pickupTime = binding.newDeliveryTimeInput.getText().toString();

        deliveryOrder.pickupLatitude = pickUpLatitude;
        deliveryOrder.pickupLongitude = pickUpLongitude;
        deliveryOrder.pickupLocation = pickUpAddress;

        deliveryOrder.dropOffLatitude = dropOffLatitude;
        deliveryOrder.dropOffLongitude = dropOffLongitude;
        deliveryOrder.dropOffLocation = dropOffAddress;

        // Added this in after everything else was done - a bit lazy approach since I didn't want to put error messages for each input like the sign in and sign up
        if (deliveryOrder.receiverName.isEmpty() || deliveryOrder.senderName.isEmpty()|| deliveryOrder.pickupDate.isEmpty() || deliveryOrder.pickupTime.isEmpty() || deliveryOrder.pickupLocation.isEmpty() ){

            Toast toast = new Toast(requireContext());
            toast.setText("Please fill in all fields");
            toast.show();

            return;
        }

        // Move on to the next page
        NavDirections action = NewDeliveryFragmentPageOneDirections.actionNewDeliveryFragmentPageOneToNewDeliveryFragmentPageTwo();
        Navigation.findNavController(view).navigate(action);
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