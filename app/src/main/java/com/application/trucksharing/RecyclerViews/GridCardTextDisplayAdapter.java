package com.application.trucksharing.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.trucksharing.R;
import java.util.ArrayList;

public class GridCardTextDisplayAdapter extends RecyclerView.Adapter<CardWithTextItemView> {

    private final Context context;
    private ArrayList<String> cardTexts;

    public GridCardTextDisplayAdapter(Context context, ArrayList<String> cardTexts) {

        this.context = context;
        this.cardTexts = cardTexts;
    }

    @NonNull
    @Override
    public CardWithTextItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_with_text, parent, false);
        return new CardWithTextItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardWithTextItemView holder, int position) {

        String text = cardTexts.get(position);
        holder.getItemCardTextView().setText(text);
    }

    @Override
    public int getItemCount() {

        return cardTexts.size();
    }
}