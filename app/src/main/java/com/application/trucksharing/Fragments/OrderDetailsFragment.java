package com.application.trucksharing.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.application.trucksharing.R;


public class OrderDetailsFragment extends Fragment {

    public OrderDetailsFragment() {


    }

    // TODO: Rename and change types and number of parameters
    public static OrderDetailsFragment newInstance() {

        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }
}