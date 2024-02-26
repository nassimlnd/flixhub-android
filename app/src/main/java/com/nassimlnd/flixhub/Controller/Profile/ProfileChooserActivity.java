package com.nassimlnd.flixhub.Controller.Profile;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nassimlnd.flixhub.Controller.Auth.Register.RegisterInterestsActivity;
import com.nassimlnd.flixhub.Controller.Profile.Fragments.ProfileCardFragment;
import com.nassimlnd.flixhub.Model.Profile;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * This activity is used to choose the user's profile
 */
public class ProfileChooserActivity extends AppCompatActivity {

    // Constants
    final Calendar calendar = Calendar.getInstance();

    // View elements
    FlexboxLayout profileAddButton, profileCardsLayout;
    EditText registerProfileBirthdate, registerProfileName;
    ImageView avatarImageView, editModeButton;

    // Data
    private ArrayList<Profile> profiles;
    private ArrayList<ProfileCardFragment> profileCardFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chooser);

        // Initialize view elements
        profileCardsLayout = findViewById(R.id.profileContainer);
        profileAddButton = findViewById(R.id.profileAddButton);
        editModeButton = findViewById(R.id.editModeButton);

        // Getting profiles of the user from the back-end
        profiles = Profile.getProfiles(this);

        // For each profile, create a card
        for (Profile profile : profiles) {
            Log.d("Profile", "onCreate: " + profile.getInterests());
            ProfileCardFragment profileCardFragment = new ProfileCardFragment(profile);

            getSupportFragmentManager().beginTransaction().add(R.id.profileContainer, profileCardFragment).commit();

            profileCardFragments.add(profileCardFragment);
        }



        // If there are already 8 profiles, hide the add button
        if (profiles.size() == 8) {
            profileAddButton.setVisibility(View.GONE);
        }

        // Modal to add a new profile
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.modal_create_profile);

        // Initialize bottom sheet view elements
        avatarImageView = bottomSheetDialog.findViewById(R.id.avatarImageView);
        registerProfileBirthdate = bottomSheetDialog.findViewById(R.id.registerProfileBirthdate);
        registerProfileName = bottomSheetDialog.findViewById(R.id.registerProfileName);

        // Setting up the default avatar
        Glide.with(avatarImageView.getContext())
                .load("https://api.nassimlounadi.fr/avatars/avatar1.png")
                .transition(withCrossFade())
                .into(avatarImageView);

        avatarImageView.setClipToOutline(true);

        profileAddButton.setOnClickListener(v -> {
            bottomSheetDialog.show();
            bottomSheetDialog.findViewById(R.id.registerProfileCancel).setOnClickListener(v1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.findViewById(R.id.registerProfileSubmit).setOnClickListener(v1 -> {
                SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
                editor.putString("name", registerProfileName.getText().toString());
                editor.putString("birthdate", registerProfileBirthdate.getText().toString());
                editor.putString("avatar", "avatar1.png");
                editor.apply();

                bottomSheetDialog.dismiss();
                Intent intent = new Intent(this, RegisterInterestsActivity.class);
                startActivity(intent);
            });
        });

        editModeButton.setOnClickListener(v -> {
            for (ProfileCardFragment profileCardFragment : profileCardFragments) {
                profileCardFragment.setEditMode(!profileCardFragment.isEditMode());
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("ProfileChooserActivity", "handleOnBackPressed: " + profileCardFragments.size());
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        registerProfileBirthdate.setOnClickListener(v -> {
            new DatePickerDialog(bottomSheetDialog.getContext(), R.style.DatePicker_Flix, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    public void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.FRANCE);
        registerProfileBirthdate.setText(sdf.format(calendar.getTime()));
    }
}
