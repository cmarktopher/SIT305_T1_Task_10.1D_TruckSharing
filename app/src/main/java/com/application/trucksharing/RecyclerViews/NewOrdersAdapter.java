package com.application.trucksharing.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.DataModels.DeliveryOrder;
import com.application.trucksharing.Fragments.NewDeliveryFragmentPageOne;
import com.application.trucksharing.Fragments.OrderDetailsFragment;
import com.application.trucksharing.R;
import java.util.ArrayList;
import java.util.List;

public class NewOrdersAdapter extends RecyclerView.Adapter<TrucksItemView> {

    private Context context;
    private List<DeliveryOrder> newOrders = new ArrayList<>();

    public NewOrdersAdapter(Context context, List<DeliveryOrder> newOrders) {

        this.context = context;
        this.newOrders = newOrders;
    }

    @NonNull
    @Override
    public TrucksItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.truck_item_card, parent, false);

        return new TrucksItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrucksItemView holder, int position) {

        DeliveryOrder newOrder = newOrders.get(position);

        holder.getTruckItemTitleTextView().setText(newOrder.receiverName);

        holder.getItemCardView().setOnClickListener(cardView -> {

            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.transition_in, R.anim.transition_out, R.anim.transition_in, R.anim.transition_out)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.coreFragmentContainer, OrderDetailsFragment.newInstance(), null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {

        return newOrders.size();
    }
}