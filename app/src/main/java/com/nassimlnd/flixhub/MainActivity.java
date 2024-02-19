package com.nassimlnd.flixhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nassimlnd.flixhub.Home.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If the user is already logged in, redirect to the HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // If the user is not logged in, redirect to the GettingStartedActivity
            startActivity(new Intent(this, GettingStartedActivity.class));
        }
    }
}
