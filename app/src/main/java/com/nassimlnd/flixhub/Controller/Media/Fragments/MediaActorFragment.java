package com.nassimlnd.flixhub.Controller.Media.Fragments;

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
import com.nassimlnd.flixhub.R;

import java.util.HashMap;

public class MediaActorFragment extends Fragment {

    TextView actorName;
    TextView actorCharacter;
    ImageView profileImage;
    HashMap<String, String> data;

    public MediaActorFragment(HashMap<String, String> data) {
        super(R.layout.fragment_media_actor);
        this.data = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_media_actor, container, false);

        actorName = view.findViewById(R.id.actorName);
        actorCharacter = view.findViewById(R.id.actorCharacter);
        profileImage = view.findViewById(R.id.profileImage);

        actorName.setText(data.get("name"));
        actorCharacter.setText(data.get("character"));

        if (data.get("profile_path") != null && !data.get("profile_path").equals("null")) {
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w500" + data.get("profile_path"))
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.user_round);
        }

        return view;
    }
}
