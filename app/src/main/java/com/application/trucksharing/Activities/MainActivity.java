package com.application.trucksharing.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.application.trucksharing.R;

/**
 * Main activity and entry point to the application.
 * Views will be handled in respective fragments.
 * Just a note regarding implementation of the Room Persistence Library, a lot of this will be based from task 7.1P.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}