package com.application.trucksharing.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.application.trucksharing.databinding.FragmentEstimateBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class EstimateFragment extends Fragment {

    // UI Binding
    FragmentEstimateBinding binding;

    // Delivery Order
    DeliveryOrder order;

    // Phone Intent
    Intent phoneCallIntent;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            // Get the data associated with this delivery order if null
            if (order == null){

                DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
                order = deliveryOrderViewModel.getCurrentSelectedOrder();
            }

            // Place some markers
            LatLng pickupLocation = new LatLng(order.pickupLatitude, order.pickupLongitude);
            LatLng dropOffLocation = new LatLng(order.dropOffLatitude, order.dropOffLongitude);
            googleMap.addMarker(new MarkerOptions().position(pickupLocation));
            googleMap.addMarker(new MarkerOptions().position(dropOffLocation));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(dropOffLocation));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEstimateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (order == null){

            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
            order = deliveryOrderViewModel.getCurrentSelectedOrder();
        }

        binding.estimateFragmentPickUpLocation.setText(order.pickupLocation);
        binding.estimateFragmentDropOffLocation.setText(order.dropOffLocation);

        // Create the action dial activity.
        // From some reading online, unlike ACTION_CALL, this one will not require permissions.
        phoneCallIntent = new Intent(Intent.ACTION_DIAL);
        phoneCallIntent.setData(Uri.parse("tel:" + 123));

        // Bind buttons
        binding.estimateFragmentCallDriverButton.setOnClickListener(this::onCallDriver);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    /**
     * Handle the call activity once the call driver button is pressed.
     * @param view View pressed.
     */
    private void onCallDriver(View view){

        // Create a new phone call intent
        startActivity(phoneCallIntent);

    }
}