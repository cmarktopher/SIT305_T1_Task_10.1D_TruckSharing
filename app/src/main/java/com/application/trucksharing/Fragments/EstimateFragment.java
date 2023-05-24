package com.application.trucksharing.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * There will be quite a lot going on in this fragment.
 * It handles the map logic - along with making a Directions API request and handling the response object for directions and travel time.
 * I will note that I am aware that I am processing the json objects and drawing the route via poly lines on the main thread.
 * There is a slight delay but at this point in time, I don't think it is worth re-working this code to be handled on a background thread.
 * The next feature this fragment handles is the paypal payment.
 */
public class EstimateFragment extends Fragment {

    // UI Binding
    FragmentEstimateBinding binding;

    // Google maps ref
    GoogleMap currentGoogleMap;

    // API Key
    String apiKey = "";

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
         * @param googleMap Google Map object.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {

            // Cache the google maps object for use later
            currentGoogleMap = googleMap;

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

            // Just to ensure we have an api key when this callback is fired
            if (apiKey.isEmpty()){

                getAPIKey();
            }

            // Handle directions
            handleDirectionsAPIRequest(
                    "origin=" + pickupLocation.latitude + "," + pickupLocation.longitude,
                    "destination=" + dropOffLocation.latitude + "," + dropOffLocation.longitude,
                    "key=" + apiKey
                    );
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEstimateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get the current delivery order
        if (order == null){

            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(requireActivity()).get(DeliveryOrderViewModel.class);
            order = deliveryOrderViewModel.getCurrentSelectedOrder();
        }

        // Get API key
        if (apiKey.isEmpty()){

            getAPIKey();
        }

        // Set the pick up and drop off locations.
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

    private void getAPIKey(){

        try {
            ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);

            if (applicationInfo != null){

                apiKey = applicationInfo.metaData.getString("com.google.android.geo.API_KEY");
            }


        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
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

    /**
     * Handles making the get request to directions API via Volley.
     * @param origin Origin point
     * @param destination Destination point
     * @param apiKey API key
     */
    private void handleDirectionsAPIRequest(String origin, String destination, String apiKey){

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String objectUrl = "https://maps.googleapis.com/maps/api/directions/json?" + origin + "&" + destination + "&" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                objectUrl,
                null,
                response -> {

                    handleDirections(response);
                    handleTravelDurationCalculation(response);
                },
                error -> {

                    Log.d("ERROR", "Error");
                    error.printStackTrace();
                }
        );

        queue.add(jsonObjectRequest);
    }

    /**
     * Handles drawing the directions on the map based on the information from the response object.
     * @param response Response obtained from the directions api.
     */
    private void handleDirections(JSONObject response){

        try {

            // Get the routes that came from the response
            JSONArray routesJsonArray = response.getJSONArray("routes");

            // Get the route object - I believe we can get multiple routes - I haven't looked to deeply into this aspect, we only really need one route anyway.
            JSONObject routeJSONObject = routesJsonArray.getJSONObject(0);

            // Get the encoded polyline object
            JSONObject overviewPolylineJSONObject = routeJSONObject.getJSONObject("overview_polyline");

            // Get the actual encoded polyline from the object.
            String polyline = overviewPolylineJSONObject.getString("points");

            // Using the utility class from google, we can easily decode the encoded string into a list of LatLng objects
            List<LatLng> line = PolyUtil.decode(polyline);

            // Use this to draw poly lines representing our route/path on the map
            currentGoogleMap.addPolyline(new PolylineOptions().addAll(line));



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles getting the travel time from the response JSON object.
     * @param response Response obtained from the directions api.
     */
    @SuppressLint("SetTextI18n")
    private void handleTravelDurationCalculation(JSONObject response){

        try {

            // Get the routes
            JSONArray routesJsonArray = response.getJSONArray("routes");

            // Get the route object
            JSONObject routeJSONObject = routesJsonArray.getJSONObject(0);

            // Get the legs - to my understanding, we will only have one leg since we didn't specify waypoints in the API request
            JSONArray legsJsonArray = routeJSONObject.getJSONArray("legs");

            // Get the first leg
            JSONObject legsJSONObject = legsJsonArray.getJSONObject(0);

            // Get the duration from the leg (this also has other information such as distance etc..)
            JSONObject durationJsonObject = legsJSONObject.getJSONObject("duration");

            // Convert to string and set to our UI
            String durationText = durationJsonObject.getString("text");
            binding.estimateFragmentTravelTimeTextView.setText("Approx. travel time: " + durationText);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePayments(){

    }
}