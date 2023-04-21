package com.application.trucksharing.RecyclerViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trucksharing.DataModels.AvailableTruck;
import com.application.trucksharing.R;

import java.util.ArrayList;
import java.util.List;

public class AvailableTrucksAdapter extends RecyclerView.Adapter<TrucksItemView> {

    private Context context;
    private List<AvailableTruck> availableTrucks = new ArrayList<>();

    public AvailableTrucksAdapter(Context context, List<AvailableTruck> availableTrucks) {

        this.context = context;
        this.availableTrucks = availableTrucks;
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

        AvailableTruck availableTruck = availableTrucks.get(position);

        holder.getTruckItemTitleTextView().setText(availableTruck.truckName);
    }

    @Override
    public int getItemCount() {

        return availableTrucks.size();
    }
}
