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

            Profile newProfile = Profile.createProfile(data, getApplicationContext());

            Log.d("RegisterInterestsActivity", "onSubmit: " + newProfile.toString());

            if (newProfile.getName().equals(sharedPreferences.getString("name", ""))) {
                editor.clear();
                editor.putString("name", newProfile.getName());
                editor.putString("avatar", newProfile.getAvatar());
                editor.putString("movieInterests", newProfile.getMovieInterests());
                editor.putString("serieInterests", newProfile.getSerieInterests());

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        });

        registerInterestsSubmitButton.setOnClickListener(v -> onSubmit());

        getMoviesCategories();
    }

    public void getMoviesCategories() {
        ArrayList<MovieCategory> movieCategories = MovieCategory.getAll(this);
        for (MovieCategory movieCategory : movieCategories) {
            InterestFragment interestFragment = new InterestFragment(movieCategory);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.interest_fragment_container, interestFragment)
                    .commit();
            interestFragments.add(interestFragment);
        }
    }

    public void onSubmit() {
        ArrayList<String> selectedMovieInterests = new ArrayList<>();
        ArrayList<String> selectedSerieInterests = new ArrayList<>();
        for (InterestFragment interestFragment : interestFragments) {
            if (interestFragment.isSelected()) {
                String categoryType = interestFragment.getCategoryType();
                switch (categoryType) {
                    case "movie":
                        selectedMovieInterests.add(String.valueOf(interestFragment.getMovieCategory().getId()));
                        break;
                    case "serie":
                        selectedSerieInterests.add(String.valueOf(interestFragment.getSerieCategory().getId()));
                        break;
                }
            }
        }

        if (selectedMovieInterests.size() < 3) {
            Toast.makeText(this, R.string.register_movie_interests_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSerieInterests.size() < 3) {
            Toast.makeText(this, R.string.register_serie_interests_error, Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedSerieInterestsJSON = new Gson().toJson(selectedSerieInterests);
        String selectedMovieInterestsJSON = new Gson().toJson(selectedMovieInterests);

        SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
        editor.putString("movieInterests", selectedMovieInterestsJSON);
        editor.putString("serieInterests", selectedSerieInterestsJSON);
        editor.putBoolean("haveMovieInterests", true);
        editor.putBoolean("haveSerieInterests", true);
        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        HashMap<String, String> data = new HashMap<>();
        data.put("name", sharedPreferences.getString("name", ""));
        data.put("avatar", sharedPreferences.getString("avatar", "avatar1.png"));
        data.put("birthdate", sharedPreferences.getString("birthdate", ""));
        data.put("movieInterests", selectedMovieInterestsJSON);
        data.put("serieInterests", selectedSerieInterestsJSON);
        data.put("haveMovieInterests", String.valueOf(sharedPreferences.getBoolean("haveMovieInterests", false)));
        data.put("haveSerieInterests", String.valueOf(sharedPreferences.getBoolean("haveSerieInterests", false)));

        Profile newProfile = Profile.createProfile(data, getApplicationContext());

        Log.d("RegisterInterestsActivity", "onSubmit: " + newProfile.toString());

        if (newProfile.getName().equals(sharedPreferences.getString("name", ""))) {
            editor.clear();
            editor.putString("name", newProfile.getName());
            editor.putString("avatar", newProfile.getAvatar());
            editor.putString("birthdate", newProfile.getBirthdate());
            editor.putString("movieInterests", newProfile.getMovieInterests());
            editor.putString("serieInterests", newProfile.getSerieInterests());
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}


