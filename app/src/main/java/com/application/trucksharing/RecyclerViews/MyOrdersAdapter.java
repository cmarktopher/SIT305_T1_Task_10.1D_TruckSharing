package com.application.trucksharing.RecyclerViews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<GeneralItemView> {

    private final FragmentActivity activity;
    private List<DeliveryOrder> myOrders = new ArrayList<>();

    public MyOrdersAdapter(FragmentActivity activity) {

        this.activity = activity;
    }

    @NonNull
    @Override
    public GeneralItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.view_general_item_card, parent, false);

        return new GeneralItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralItemView holder, int position) {

        DeliveryOrder newOrder = myOrders.get(position);

        String message = "Delivery to be received by " + newOrder.receiverName + " from " + newOrder.senderName + " at " + newOrder.pickupTime;

        // Set the UI elements for an order/delivery
        holder.getTruckItemTitleTextView().setText(newOrder.senderName);
        holder.getItemDescriptionTextView().setText(message);

        // Bind to share button
        holder.getItemShareButton().setOnClickListener(shareButtonView -> {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            activity.startActivity(shareIntent);
        });

        // Set an on click listener to the card itself
        holder.getItemCardView().setOnClickListener(cardView -> {

            // Set the order/delivery that was selected so that we can access the details in the fragment we are transitioning to
            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(activity).get(DeliveryOrderViewModel.class);
            deliveryOrderViewModel.setCurrentSelectedOrder(myOrders.get(position));

            // Do the transition
            NavDirections action = com.application.trucksharing.Fragments.MyOrdersFragmentDirections.actionMyOrdersFragmentToOrderDetailsFragment();
            Navigation.findNavController(cardView).navigate(action);
        });
    }

    @Override
    public int getItemCount() {

        return myOrders.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateOrder(List<DeliveryOrder> newOrders){
        myOrders = newOrders;
        notifyDataSetChanged();
    }
}