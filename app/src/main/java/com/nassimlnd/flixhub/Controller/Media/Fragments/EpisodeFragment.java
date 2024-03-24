package com.nassimlnd.flixhub.Controller.Media.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Model.Episode;
import com.nassimlnd.flixhub.R;

public class EpisodeFragment extends Fragment {

    // View elements
    ImageView episodePoster;
    TextView episodeName;

    // Data
    private Episode episode;

    public EpisodeFragment(Episode episode) {
        super(R.layout.fragment_episode);
        this.episode = episode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);

        Log.d("EPISODE", "onCreateView: " + "episode fragment created");

        episodePoster = view.findViewById(R.id.episodePoster);
        episodeName = view.findViewById(R.id.episodeName);

        episodePoster.setBackground(AppCompatResources.getDrawable(episodePoster.getContext(), R.drawable.media_card));
        episodePoster.setClipToOutline(true);
        episodeName.setText("Episode " + episode.getEpisodeNumber());

        Glide.with(episodePoster.getContext()).load(episode.getPoster()).into(episodePoster);

        return view;
    }
}
