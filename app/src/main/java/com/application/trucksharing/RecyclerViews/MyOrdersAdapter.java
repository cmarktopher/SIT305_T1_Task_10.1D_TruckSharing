package com.application.trucksharing.RecyclerViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.Fragments.OrderDetailsFragment;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.DeliveryOrderViewModel;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<GeneralItemView> {

    private FragmentActivity activity;
    private List<DeliveryOrder> myOrders = new ArrayList<>();

    public MyOrdersAdapter(FragmentActivity activity) {

        this.activity = activity;
    }

    @NonNull
    @Override
    public GeneralItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.general_item_card, parent, false);

        return new GeneralItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralItemView holder, int position) {

        DeliveryOrder newOrder = myOrders.get(position);

        // Set the UI elements for an order/delivery
        holder.getTruckItemTitleTextView().setText(newOrder.receiverName);

        // Set an on click listener to the card itself
        holder.getItemCardView().setOnClickListener(cardView -> {

            // Set the order/delivery that was selected so that we can access the details in the fragment we are transitioning to
            DeliveryOrderViewModel deliveryOrderViewModel = new ViewModelProvider(activity).get(DeliveryOrderViewModel.class);
            deliveryOrderViewModel.setCurrentSelectedOrder(myOrders.get(position));

            // Instantiate the fragment
            OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance();

            // Handle the container transform animation
            String transitionName = "card_transition_" + newOrder.uid;
            ViewCompat.setTransitionName(holder.getTruckItemConstraintLayout(), transitionName);
            MaterialContainerTransform transform = new MaterialContainerTransform();
            transform.setDuration(600);
            transform.setFadeMode(MaterialContainerTransform.FADE_MODE_OUT);
            orderDetailsFragment.setSharedElementEnterTransition(transform);


            FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addSharedElement(holder.getTruckItemConstraintLayout(), transitionName)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.coreFragmentContainer, orderDetailsFragment , null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {

        return myOrders.size();
    }

    public void UpdateOrder(List<DeliveryOrder> newOrders){
        myOrders = newOrders;
        notifyDataSetChanged();
    }
}