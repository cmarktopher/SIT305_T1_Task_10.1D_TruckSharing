package com.application.trucksharing.RecyclerViews;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trucksharing.R;

public class CardWithTextItemView extends RecyclerView.ViewHolder {

    private final CardView itemCardView;
    private final TextView itemCardText;

    public CardWithTextItemView(@NonNull View itemView) {

        super(itemView);

        itemCardView  = itemView.findViewById(R.id.cardWithTextView);
        itemCardText = itemView.findViewById(R.id.cardTextView);
    }

    public CardView getItemCardView() {
        return itemCardView;
    }
    public TextView getItemCardTextView() {
        return itemCardText;
    }
}