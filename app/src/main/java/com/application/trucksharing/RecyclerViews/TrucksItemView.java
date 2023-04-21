package com.application.trucksharing.RecyclerViews;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.trucksharing.R;

public class TrucksItemView extends RecyclerView.ViewHolder {

    private final TextView truckItemTitleTextView;

    public TrucksItemView(@NonNull View itemView) {

        super(itemView);

        truckItemTitleTextView = itemView.findViewById(R.id.truckItemTitleTextView);
    }

    public TextView getTruckItemTitleTextView() {
        return truckItemTitleTextView;
    }
}