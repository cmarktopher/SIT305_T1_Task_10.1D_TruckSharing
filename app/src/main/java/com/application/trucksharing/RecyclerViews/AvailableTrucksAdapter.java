package com.application.trucksharing.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Available truck adapter for our truck recycler view.
 * I assume this is location based and would have trucks within location appear.
 * For the moment, since we don't have a truck user interface for truck drivers to sign up (the sign up form seems to be for regular users and not catered to the truck drivers)
 * I'll just add in default entries into the database.
 * At this stage, location functionality is not implemented.
 */
public class AvailableTrucksAdapter extends RecyclerView.Adapter<GeneralItemView> {

    private final Context context;
    private List<AvailableTruck> availableTrucks = new ArrayList<>();

    public AvailableTrucksAdapter(Context context, List<AvailableTruck> availableTrucks) {

        this.context = context;
        this.availableTrucks = availableTrucks;
    }

    @NonNull
    @Override
    public GeneralItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_general_item_card, parent, false);

        return new GeneralItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralItemView holder, int position) {

        AvailableTruck availableTruck = availableTrucks.get(position);

        // Fill with data
        holder.getTruckItemTitleTextView().setText(availableTruck.truckName);
        holder.getItemDescriptionTextView().setText(availableTruck.truckDescription);

        // Need to bind the share button
        holder.getItemShareButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, availableTruck.truckName);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return availableTrucks.size();
    }
}
