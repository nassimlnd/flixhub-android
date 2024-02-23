package com.nassimlnd.flixhub.Controller.Home.Fragments.Discover.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.R;
import com.nassimlnd.flixhub.Controller.Media.MediaActivity;

public class SearchResultFragment extends Fragment {

    FlexboxLayout searchResultContainer;

    TextView searchResultTitle;
    TextView searchResultGroup;
    ImageView searchResultImage;

    Media media;

    public SearchResultFragment(Media media) {
        super(R.layout.fragment_search_result);
        this.media = media;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        searchResultTitle = view.findViewById(R.id.searchResultTitle);
        searchResultGroup = view.findViewById(R.id.searchResultGroup);
        searchResultImage = view.findViewById(R.id.searchResultImage);
        searchResultContainer = view.findViewById(R.id.searchResultLayout);

        searchResultContainer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MediaActivity.class);
            intent.putExtra("mediaId", media.getId());

            startActivity(intent);
        });

        searchResultTitle.setText(media.getTvg_name());
        searchResultGroup.setText(media.getGroup_title());

        Glide.with(view.getContext())
                .load(media.getTvg_logo())
                .into(searchResultImage);

        return view;
    }
}
