package com.application.trucksharing.RecyclerViews;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.application.trucksharing.R;

public class GeneralItemView extends RecyclerView.ViewHolder {
    private final ConstraintLayout truckItemConstraintLayout;
    private final CardView itemCardView;
    private final TextView itemTitleTextView;
    private final TextView itemDescriptionTextView;
    private final ImageButton itemShareButton;

    public GeneralItemView(@NonNull View itemView) {

        super(itemView);
        truckItemConstraintLayout = itemView.findViewById(R.id.truckItemConstraintLayout);
        itemCardView  = itemView.findViewById(R.id.truckItemCard);
        itemTitleTextView = itemView.findViewById(R.id.generalItemTitleTextView);
        itemDescriptionTextView = itemView.findViewById(R.id.generalItemDescription);
        itemShareButton = itemView.findViewById(R.id.itemShareButton);
    }

    public ConstraintLayout getTruckItemConstraintLayout() { return truckItemConstraintLayout; }
    public CardView getItemCardView() {
        return itemCardView;
    }
    public TextView getTruckItemTitleTextView() {
        return itemTitleTextView;
    }
    public TextView getItemDescriptionTextView() { return itemDescriptionTextView;}
    public ImageButton getItemShareButton() { return itemShareButton; }
}