package com.nassimlnd.flixhub.Controller.Auth.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.nassimlnd.flixhub.Controller.Auth.Register.Fragments.InterestFragment;
import com.nassimlnd.flixhub.Controller.Home.HomeActivity;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.Model.Profile;
import com.nassimlnd.flixhub.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterInterestsActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button registerInterestsSkipButton, registerInterestsSubmitButton;
    ArrayList<InterestFragment> interestFragments = new ArrayList<>();
    private ArrayList<String> topics = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interests);

        toolbar = findViewById(R.id.toolbar);
        registerInterestsSkipButton = findViewById(R.id.registerInterestsSkipButton);
        registerInterestsSubmitButton = findViewById(R.id.registerInterestsSubmitButton);

        toolbar.setTitle(R.string.register_interests_title);
        toolbar.setTitleTextColor(Color.WHITE);

        registerInterestsSkipButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
            editor.putBoolean("haveInterests", false);
            editor.apply();

            SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
            HashMap<String, String> data = new HashMap<>();
            data.put("name", sharedPreferences.getString("name", ""));
            data.put("avatar", sharedPreferences.getString("avatar", "avatar1.png"));
            data.put("birthdate", sharedPreferences.getString("birthdate", ""));
            //data.put("haveInterests", String.valueOf(sharedPreferences.getBoolean("haveInterests", false)));

            Profile newProfile = Profile.createProfile(data, getApplicationContext());

            if (newProfile.getName().equals(sharedPreferences.getString("name", ""))) {
                editor.clear();
                editor.putString("name", newProfile.getName());
                editor.putString("avatar", newProfile.getAvatar());
                editor.putString("interests", newProfile.getInterests());
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        });

        registerInterestsSubmitButton.setOnClickListener(v -> onSubmit());

        getMoviesCategories();

        Log.d("TAG", "onCreate: " + topics.size());
    }

    public void getMoviesCategories() {
        ArrayList<MovieCategory> movieCategories = MovieCategory.getAll(this);
        for (MovieCategory movieCategory : movieCategories) {
            InterestFragment interestFragment = new InterestFragment(movieCategory.getName());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.interest_fragment_container, interestFragment)
                    .commit();
            interestFragments.add(interestFragment);
        }
    }

    public void onSubmit() {
        ArrayList<String> selectedInterests = new ArrayList<>();
        for (InterestFragment interestFragment : interestFragments) {
            if (interestFragment.isSelected()) {
                selectedInterests.add(interestFragment.text);
            }
        }

        if (selectedInterests.size() < 3) {
            Toast.makeText(this, R.string.register_interests_error, Toast.LENGTH_SHORT).show();
            return;
        }

        String json = new Gson().toJson(selectedInterests);

        SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
        editor.putString("interests", json);
        editor.putBoolean("haveInterests", true);
        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        HashMap<String, String> data = new HashMap<>();
        data.put("name", sharedPreferences.getString("name", ""));
        data.put("avatar", sharedPreferences.getString("avatar", "avatar1.png"));
        data.put("birthdate", sharedPreferences.getString("birthdate", ""));
        data.put("interests", json);
        data.put("haveInterests", String.valueOf(sharedPreferences.getBoolean("haveInterests", false)));

        Profile newProfile = Profile.createProfile(data, getApplicationContext());

        if (newProfile.getName().equals(sharedPreferences.getString("name", ""))) {
            editor.clear();
            editor.putString("name", newProfile.getName());
            editor.putString("avatar", newProfile.getAvatar());
            editor.putString("birthdate", newProfile.getBirthdate());
            editor.putString("interests", newProfile.getInterests());
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}


