package com.nassimlnd.flixhub.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nassimlnd.flixhub.Controller.Home.HomeActivity;

/**
 * This activity is used to check if the user is already logged in or not
 * If the user is already logged in, redirect to the HomeActivity
 * If the user is not logged in, redirect to the GettingStartedActivity
 */
public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isLoggedIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If the user is already logged in, redirect to the HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // If the user is not logged in, redirect to the GettingStartedActivity
            startActivity(new Intent(this, GettingStartedActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Fix the issue of the user being able to go back to the MainActivity, and shows them a blank screen

        if (isLoggedIn) {
            // If the user is already logged in, redirect to the HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // If the user is not logged in, redirect to the GettingStartedActivity
            startActivity(new Intent(this, GettingStartedActivity.class));
        }
    }
}
