package com.application.trucksharing.RecyclerViews;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.application.trucksharing.R;

public class TrucksItemView extends RecyclerView.ViewHolder {

    private final CardView itemCardView;
    private final TextView truckItemTitleTextView;

    public TrucksItemView(@NonNull View itemView) {

        super(itemView);
        itemCardView  = itemView.findViewById(R.id.truckItemCard);
        truckItemTitleTextView = itemView.findViewById(R.id.truckItemTitleTextView);
    }

    public CardView getItemCardView() {
        return itemCardView;
    }
    public TextView getTruckItemTitleTextView() {
        return truckItemTitleTextView;
    }
}