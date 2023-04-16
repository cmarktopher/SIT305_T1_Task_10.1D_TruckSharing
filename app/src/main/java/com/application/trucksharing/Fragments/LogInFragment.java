package com.application.trucksharing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.trucksharing.R;

/**
 * Fragment for the log in page - this will also be the entry view the user sees when opening the app
 */
public class LogInFragment extends Fragment {

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance() {

        LogInFragment fragment = new LogInFragment();
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

        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }
}