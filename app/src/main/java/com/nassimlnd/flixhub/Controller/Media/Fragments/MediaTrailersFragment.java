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

public class MediaTrailersFragment extends Fragment {

    private String trailerTitle;
    private String trailerDuration;
    private String trailerImage;

    ImageView imageTrailer;
    TextView titleTrailer;
    TextView durationTrailer;

    public MediaTrailersFragment(String trailerTitle, String trailerDuration, String trailerImage) {
        super(R.layout.fragment_media_trailer);

        this.trailerDuration = trailerDuration;
        this.trailerTitle = trailerTitle;
        this.trailerImage = trailerImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_media_trailer, container, false);

        imageTrailer = view.findViewById(R.id.trailerImage);
        titleTrailer = view.findViewById(R.id.trailerTitle);
        durationTrailer = view.findViewById(R.id.trailerDuration);

        titleTrailer.setText(trailerTitle);
        durationTrailer.setText(trailerDuration);

        Glide.with(imageTrailer.getContext())
                .load(trailerImage)
                .into(imageTrailer);

        return view;
    }
}
