package com.nassimlnd.flixhub.Controller.Profile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nassimlnd.flixhub.Controller.Profile.Fragments.ProfileMediaHistoryFragment;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

public class ProfileHistoryActivity extends AppCompatActivity {

    // View elements
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_history);

        // Get the view elements
        toolbar = findViewById(R.id.toolbar);

        // Set the toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.profile_history_title);
        toolbar.setTitleTextColor(Color.WHITE);

        loadProfileHistory();

    }

    private void loadProfileHistory() {
        ArrayList<Movie> movies = Movie.getMovieHistoryByProfile(getApplicationContext());

        // Load the movies fragments
        for (Movie movie : movies) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ll_profile_history, new ProfileMediaHistoryFragment(movie))
                    .commit();
        }
    }
}
