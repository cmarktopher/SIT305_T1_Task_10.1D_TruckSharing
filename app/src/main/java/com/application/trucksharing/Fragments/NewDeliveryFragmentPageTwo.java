package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.trucksharing.R;

/**
 * Page 2 for the new delivery fragment
 */
public class NewDeliveryFragmentPageTwo extends Fragment {

    public NewDeliveryFragmentPageTwo() {


    }

    public static NewDeliveryFragmentPageTwo newInstance() {

        NewDeliveryFragmentPageTwo fragment = new NewDeliveryFragmentPageTwo();
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_delivery_page_two, container, false);
    }
}