package com.nassimlnd.flixhub.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nassimlnd.flixhub.R;

public class GettingStartedActivity extends AppCompatActivity {

    Button getStartedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LogChoiceActivity.class));
        });
    }
}
