package com.nassimlnd.flixhub.Controller.Profile.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Controller.Media.MediaActivity;
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.R;

public class ProfileMediaHistoryFragment extends Fragment {

    private static final String TAG = "ProfileMediaHistoryFragm";
    // View elements
    TextView mediaTitle, mediaGroup;
    ImageView mediaImage;
    LinearLayout profileHistoryMediaContainer;

    // Data
    private Movie movie;

    public ProfileMediaHistoryFragment(Movie movie) {
        super(R.layout.fragment_profile_history_media);
        this.movie = movie;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile_history_media, container, false);

        // Get the view elements
        mediaTitle = view.findViewById(R.id.tv_profile_history_media_title);
        mediaGroup = view.findViewById(R.id.tv_profile_history_media_group);
        mediaImage = view.findViewById(R.id.iv_profile_history_media);
        profileHistoryMediaContainer = view.findViewById(R.id.ll_profile_history_media);

        // Set the data
        mediaTitle.setText(movie.getTitle());
        mediaGroup.setText(movie.getGroup_title());

        // Load the image
        mediaImage.setClipToOutline(true);
        Glide.with(mediaImage.getContext())
                .load(movie.getTvg_logo())
                .transition(withCrossFade())
                .into(mediaImage);

        // Set the click listener
        profileHistoryMediaContainer.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MediaActivity.class);
            intent.putExtra("mediaId", movie.getId());

            Log.d(TAG, "onCreateView: " + movie.getId());

            Interaction interaction = new Interaction();
            interaction.setMediaId(movie.getId());
            interaction.setMediaType("movie");
            interaction.setInteractionType("click");
            interaction.sendInteraction(view.getContext());

            startActivity(intent);
        });

        return view;
    }
}
