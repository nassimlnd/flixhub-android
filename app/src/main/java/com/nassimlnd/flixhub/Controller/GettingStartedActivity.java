package com.nassimlnd.flixhub.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nassimlnd.flixhub.R;

/**
 * This activity is the Getting Started activity of the app, it's the first activity that the user will see
 * It contains a button to redirect to the LogChoiceActivity
 */
public class GettingStartedActivity extends AppCompatActivity {

    Button getStartedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        // Get the button and set the click listener
        getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LogChoiceActivity.class));
        });

        // Disable the native effect of the back button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("KEY PRESSED", "BACK");
            }
        });
    }
}
