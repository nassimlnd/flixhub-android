package com.nassimlnd.flixhub.Controller.Profile.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Home.HomeActivity;
import com.nassimlnd.flixhub.Model.Profile;
import com.nassimlnd.flixhub.R;

public class ProfileCardFragment extends Fragment {

    // Constants
    private static final String TAG = "ProfileCardFragment";
    private static final String AVATAR_URL = "https://api.nassimlounadi.fr/avatars/";

    // Data
    private Profile profile;

    // View elements
    private ImageView profileAvatar;
    private TextView profileName;
    private FlexboxLayout profileCardLayout;

    public ProfileCardFragment(Profile profile) {
        super(R.layout.fragment_profile_card);
        this.profile = profile;
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

        profileName.setText(profile.getName());

        Glide.with(profileAvatar.getContext())
                .load(AVATAR_URL + profile.getAvatar())
                .transition(withCrossFade())
                .into(profileAvatar);

        profileCardLayout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
            editor.putInt("id", profile.getId());
            editor.putString("name", profile.getName());
            editor.putString("avatar", profile.getAvatar());
            editor.putString("interests", profile.getInterests());
            editor.apply();

            requireActivity().finish();
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
