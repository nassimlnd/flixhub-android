package com.nassimlnd.flixhub.Controller.Profile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nassimlnd.flixhub.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    // View elements
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        // Get the view elements
        toolbar = findViewById(R.id.toolbar);

        // Set the toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.profile_privacy_policy);
        toolbar.setTitleTextColor(Color.WHITE);
    }
}
