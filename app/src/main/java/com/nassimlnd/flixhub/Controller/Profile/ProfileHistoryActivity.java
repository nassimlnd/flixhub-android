package com.nassimlnd.flixhub.Controller.Profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nassimlnd.flixhub.Controller.Profile.Fragments.ProfileMediaHistoryFragment;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.Profile;
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

        // Erase the profile history
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.modal_erase_history);

        // Configure the bottom sheet dialog
        Button cancelButton = bottomSheetDialog.findViewById(R.id.cancelEraseHistoryButton);
        Button eraseButton = bottomSheetDialog.findViewById(R.id.eraseHistoryButton);

        cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

        eraseButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);

            Profile profile = new Profile();
            profile.setId(sharedPreferences.getInt("id", 0));
            profile.setName(sharedPreferences.getString("name", ""));
            profile.setAvatar(sharedPreferences.getString("avatar", ""));
            profile.setBirthdate(sharedPreferences.getString("birthdate", ""));
            profile.setInterests(sharedPreferences.getString("interests", ""));
            profile.eraseProfileHistory(getApplicationContext());

            bottomSheetDialog.dismiss();
            finish();
            startActivity(getIntent());
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.profile_history_trash) {
                // Open the bottom sheet dialog
                bottomSheetDialog.show();
                return true;
            }
            return false;
        });

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
