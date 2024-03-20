package com.nassimlnd.flixhub.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nassimlnd.flixhub.Controller.Home.HomeActivity;
import com.nassimlnd.flixhub.Controller.Profile.ProfileChooserActivity;
import com.nassimlnd.flixhub.R;

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

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        String TAG = "MainActivity";
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        // Check if the user is already logged in
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If the user is already logged in, redirect to the ProfileChooserActivity
            startActivity(new Intent(this, ProfileChooserActivity.class));
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
            startActivity(new Intent(this, ProfileChooserActivity.class));
        } else {
            // If the user is not logged in, redirect to the GettingStartedActivity
            startActivity(new Intent(this, GettingStartedActivity.class));
        }
    }
}
