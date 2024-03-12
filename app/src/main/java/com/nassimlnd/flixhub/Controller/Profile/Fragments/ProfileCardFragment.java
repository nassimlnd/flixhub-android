package com.nassimlnd.flixhub.Controller.Profile.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nassimlnd.flixhub.Controller.Home.HomeActivity;
import com.nassimlnd.flixhub.Model.Profile;
import com.nassimlnd.flixhub.R;

import java.util.Calendar;
import java.util.Locale;

public class ProfileCardFragment extends Fragment {

    // Constants
    private static final String TAG = "ProfileCardFragment";
    private static final String AVATAR_URL = "https://api.nassimlounadi.fr/avatars/";
    final Calendar calendar = Calendar.getInstance();

    // Data
    private Profile profile;
    private boolean editMode;

    // View elements
    private ImageView profileAvatar;
    private TextView profileName, editProfileBirthdate;
    private FlexboxLayout profileCardLayout, editLayout;
    private RelativeLayout profileCard;

    public ProfileCardFragment(Profile profile) {
        super(R.layout.fragment_profile_card);
        this.profile = profile;
        this.editMode = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile_card, container, false);

        profileAvatar = view.findViewById(R.id.profileAvatar);
        profileName = view.findViewById(R.id.profileName);
        profileCardLayout = view.findViewById(R.id.profileCardLayout);
        editLayout = view.findViewById(R.id.editLayout);
        profileCard = view.findViewById(R.id.profileCard);

        profileName.setText(profile.getName());

        profileAvatar.setClipToOutline(true);
        profileCard.setClipToOutline(true);


        Glide.with(profileAvatar.getContext())
                .load(AVATAR_URL + profile.getAvatar())
                .transition(withCrossFade())
                .into(profileAvatar);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(R.layout.modal_edit_profile);

        TextView editProfileTitle = bottomSheetDialog.findViewById(R.id.editProfileTitle);
        TextView editProfileName = bottomSheetDialog.findViewById(R.id.editProfileName);
        editProfileBirthdate = bottomSheetDialog.findViewById(R.id.editProfileBirthdate);
        ImageView editProfileAvatar = bottomSheetDialog.findViewById(R.id.editAvatarImage);

        Button editProfileDeleteButton = bottomSheetDialog.findViewById(R.id.editProfileEditButton);
        Button editProfileCancelButton = bottomSheetDialog.findViewById(R.id.editProfileCancelButton);
        Button editProfileSubmitButton = bottomSheetDialog.findViewById(R.id.editProfileSubmitButton);

        editProfileTitle.setText(getContext().getString(R.string.modal_edit_profile_title) + " " + profile.getName());
        editProfileName.setText(profile.getName());
        editProfileBirthdate.setText(profile.getBirthdate());

        Glide.with(editProfileAvatar.getContext())
                .load(AVATAR_URL + profile.getAvatar())
                .transition(withCrossFade())
                .into(editProfileAvatar);

        editProfileAvatar.setClipToOutline(true);

        editProfileCancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

        editProfileDeleteButton.setOnClickListener(v -> {
            boolean isDeleted = Profile.deleteProfile(getContext(), profile.getId());

            if (isDeleted) {
                bottomSheetDialog.dismiss();
                profileCardLayout.setVisibility(View.GONE);
            }
        });

        editProfileSubmitButton.setOnClickListener(v -> {
            Profile updatedProfile = new Profile();

            updatedProfile.setId(profile.getId());
            updatedProfile.setName(editProfileName.getText().toString());
            updatedProfile.setBirthdate(editProfileBirthdate.getText().toString());
            updatedProfile.setAvatar(profile.getAvatar());
            updatedProfile.setMovieInterests(profile.getMovieInterests());
            updatedProfile.setSerieInterests(profile.getSerieInterests());

            profile = Profile.updateProfile(getContext(), updatedProfile);

            editProfileTitle.setText(getContext().getString(R.string.modal_edit_profile_title) + " " + updatedProfile.getName());
            editProfileName.setText(updatedProfile.getName());
            editProfileBirthdate.setText(updatedProfile.getBirthdate());

            Glide.with(editProfileAvatar.getContext())
                    .load(AVATAR_URL + updatedProfile.getAvatar())
                    .transition(withCrossFade())
                    .into(editProfileAvatar);

            profileName.setText(updatedProfile.getName());

            Glide.with(profileAvatar.getContext())
                    .load(AVATAR_URL + updatedProfile.getAvatar())
                    .transition(withCrossFade())
                    .into(profileAvatar);


            bottomSheetDialog.dismiss();
        });


        profileCardLayout.setOnClickListener(v -> {
            if (!editMode) {
                SharedPreferences.Editor editor = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
                editor.putInt("id", profile.getId());
                editor.putString("name", profile.getName());
                editor.putString("avatar", profile.getAvatar());
                editor.putString("movieInterests", profile.getMovieInterests());
                editor.putString("serieInterests", profile.getSerieInterests());
                editor.apply();

                requireActivity().finish();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            } else {
                bottomSheetDialog.show();
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

        editProfileBirthdate.setOnClickListener(v -> {
            new DatePickerDialog(bottomSheetDialog.getContext(), R.style.DatePicker_Flix, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        return view;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        if (this.editMode) {
            editLayout.setVisibility(View.VISIBLE);
        } else {
            editLayout.setVisibility(View.GONE);
        }
    }

    public void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.FRANCE);
        editProfileBirthdate.setText(sdf.format(calendar.getTime()));
    }
}
