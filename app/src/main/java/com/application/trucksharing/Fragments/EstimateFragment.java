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
import android.widget.Toast;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * There will be quite a lot going on in this fragment.
 * It handles the map logic - along with making a Directions API request and handling the response object for directions and travel time.
 * I will note that I am aware that I am processing the json objects and drawing the route via poly lines on the main thread.
 * There is a slight delay (could be due to the emulator though) but at this point in time, I don't think it is worth re-working this code to be handled on a background thread.
 * The next feature this fragment handles is the google payment.
 * Followed this as a guide on how to do this (had to convert code from Kotlin to Java): https://developer.android.com/codelabs/pay-android-checkout#5
 * I will point out that I am doing a lot of generation of JSONObjects to make this work - I think it would be better to store these somewhere else (json file for example) but I'll keep it like this for now.
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

    // Payments related
    private PaymentsClient paymentsClient;
    private Boolean canPayUsingGooglePay = false;
    private final int PAYMENT_REQUEST_CODE = 1000;

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
        binding.estimateFragmentCallDriverButton.setOnClickListener(this::onCallDriverPressed);
        binding.estimateFragmentBookNowButton.setOnClickListener(this::onBookNowPressed);

        // Handle payments
        paymentsClient = createPaymentsClient();

        // Check if user can pay with google pay
        IsReadyToPayRequest readyToPayRequest = IsReadyToPayRequest.fromJson(getGooglePayBaseConfig().toString());
        Task<Boolean> readyToPayTask = paymentsClient.isReadyToPay(readyToPayRequest);
        readyToPayTask.addOnCompleteListener(task -> {

            try {

                // To my understanding, the result we get back will tell us if the user can use google pay or not.
                setGooglePayAvailable(task.getResult(ApiException.class));

            } catch (ApiException e) {

                Log.d("ERROR", e.getStatus().toString());
            }
        });

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
     * Creates a payments client
     * @return PaymentsClient
     */
    private PaymentsClient createPaymentsClient(){

        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build();

        return Wallet.getPaymentsClient(requireActivity(), walletOptions);
    }

    /**
     * Creates a google pay base config.
     * This will also utilize a method created below for card payment methods.
     * @return JSONObject for our base config.
     */
    private JSONObject getGooglePayBaseConfig(){

        JSONObject googlePayBaseConfig = new JSONObject();

        try {
            JSONArray baseCardPaymentMethodJsonArray = new JSONArray();
            baseCardPaymentMethodJsonArray.put(getBaseCardPaymentMethod());

            googlePayBaseConfig.put("apiVersion", 2);
            googlePayBaseConfig.put("apiVersionMinor", 0);
            googlePayBaseConfig.put("allowedPaymentMethods", baseCardPaymentMethodJsonArray);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return googlePayBaseConfig;
    }

    /**
     * Create a base card payment method.
     * @return JSONObject for our base card payment method.
     */
    private JSONObject getBaseCardPaymentMethod(){

        // Card and auth method options
        ArrayList<String> cards = new ArrayList<>();
        cards.add("VISA");
        cards.add("MASTERCARD");

        ArrayList<String> auth = new ArrayList<>();
        auth.add("PAN_ONLY");
        auth.add("CRYPTOGRAM_3DS");

        // payment method object
        JSONObject baseCardPaymentMethod = new JSONObject();

        try {
            // Create a params JSONObject
            JSONObject params = new JSONObject();
            params.put("allowedCardNetworks", new JSONArray(cards));
            params.put("allowedAuthMethods", new JSONArray(auth));

            baseCardPaymentMethod.put("type", "CARD");
            baseCardPaymentMethod.put("parameters", params);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return baseCardPaymentMethod;
    }

    /**
     * Create a card payment method.
     * @return JSONObject for our base card payment method.
     */
    private JSONObject getCardPaymentMethod(){

        // Card and auth method options
        ArrayList<String> cards = new ArrayList<>();
        cards.add("VISA");
        cards.add("MASTERCARD");

        ArrayList<String> auth = new ArrayList<>();
        auth.add("PAN_ONLY");
        auth.add("CRYPTOGRAM_3DS");

        // payment method object
        JSONObject cardPaymentMethod = new JSONObject();

        try {
            // Create a params JSONObject
            JSONObject params = new JSONObject();
            params.put("allowedCardNetworks", new JSONArray(cards));
            params.put("allowedAuthMethods", new JSONArray(auth));
            params.put("billingAddressRequired", false);

            cardPaymentMethod.put("type", "CARD");
            cardPaymentMethod.put("tokenizationSpecification", getTokenizationSpecification());
            cardPaymentMethod.put("parameters", params);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return cardPaymentMethod;
    }

    /**
     * Returns a specification of how payment processing is handled.
     * According to the docs, we need to also provide a list of gateways for specific providers - however, we can just use "example" which provides test results for transactions.
     * @return JSONObject of our tokenization specification.
     */
    private JSONObject getTokenizationSpecification(){

        // Tokenization specification json object
        JSONObject tokenizationSpecification = new JSONObject();

        try {
            // Setup the parameters we need
            JSONObject params = new JSONObject();
            params.put("gateway", "example");
            params.put("gatewayMerchantId", "exampleGatewayMerchantId");

            // Set our required values for the type and params
            tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
            tokenizationSpecification.put("parameters", params);


        } catch (JSONException e) {

            throw new RuntimeException(e);
        }

        return tokenizationSpecification;
    }

    /**
     * Get the transaction info which is basically just the information on price and currency.
     * @return JSONObject of our transaction info.
     */
    private JSONObject getTransactionInfo(){

        // Transaction Info json object
        JSONObject transactionInfo = new JSONObject();

        try {
            transactionInfo.put("totalPrice", "20");
            transactionInfo.put("totalPriceStatus", "FINAL");
            transactionInfo.put("currencyCode", "AUD");

        } catch (JSONException e) {

            throw new RuntimeException(e);
        }

        return transactionInfo;
    }

    /**
     * Get merchant info.
     * @return JSONObject of our merchant info.
     */
    private JSONObject getMerchantInfo(){

        // Merchant Info json object
        JSONObject merchantInfo = new JSONObject();

        try {

            merchantInfo.put("merchantName", "Example Merchant");
            merchantInfo.put("merchantId", "01234567890123456789");
        } catch (JSONException e) {

            throw new RuntimeException(e);
        }

        return merchantInfo;
    }

    /**
     * Handle different responses based on google pay being available or not.
     * @param available Is google pay available boolean.
     */
    private void setGooglePayAvailable(Boolean available){

        if (available) {

            // If we can use google pay, set this boolean to true.
            // When we press the book now button
            canPayUsingGooglePay = true;
        }
        else{
            canPayUsingGooglePay = false;

            // Technically, we should have other payment options and have that display appropriately.
            // But since we have a single book now button and that is tightly bound to our single payment method (which is google payment in this case), there is no point in making the button invisible or updating it.
            // Instead, let's just show a toast saying it cannot be used.
            Toast toast = new Toast(requireActivity());
            toast.setText("Google Pay is not configured.");
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Just a method to grab the api key from the meta data.
     */
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
    private void onCallDriverPressed(View view){

        // Create a new phone call intent
        startActivity(phoneCallIntent);

    }

    /**
     * Response to book now being pressed.
     * @param view View pressed.
     */
    private void onBookNowPressed(View view){

        if (canPayUsingGooglePay){

            try {

                // Put our payment methods into a JSONArray
                JSONArray cardPaymentMethodsJsonArray = new JSONArray();
                cardPaymentMethodsJsonArray.put(getCardPaymentMethod());

                // Fill our payment data request
                JSONObject paymentDataRequestJsonObject = new JSONObject(getGooglePayBaseConfig().toString());
                paymentDataRequestJsonObject.put("allowedPaymentMethods",cardPaymentMethodsJsonArray);
                paymentDataRequestJsonObject.put("transactionInfo", getTransactionInfo());
                paymentDataRequestJsonObject.put("merchantInfo", getMerchantInfo());

                // Call
                PaymentDataRequest paymentDataRequest = PaymentDataRequest.fromJson(paymentDataRequestJsonObject.toString());
                AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(paymentDataRequest), requireActivity(), PAYMENT_REQUEST_CODE);

            } catch (JSONException e) {

                Log.d("ERROR", e.getMessage());
            }
        }
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

            // I'll also set the cost here based on distance - this would obviously need proper handling in a real application
            JSONObject distanceJsonObject = legsJSONObject.getJSONObject("distance");
            int distanceValue = distanceJsonObject.getInt("value");
            int costValue = (int) (distanceValue * 0.001);
            String costString = String.valueOf(costValue);
            binding.estimateFragmentCostTextView.setText("Approx. Fare: $" + costString);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}