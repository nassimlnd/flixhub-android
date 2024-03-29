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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Media.SerieDetailsActivity;
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.Model.Serie;
import com.nassimlnd.flixhub.Model.SerieCategory;
import com.nassimlnd.flixhub.R;
import com.nassimlnd.flixhub.Controller.Media.MovieDetailsActivity;

public class SearchResultFragment extends Fragment {

    FlexboxLayout searchResultContainer;

    TextView searchResultTitle;
    TextView searchResultGroup;
    ImageView searchResultImage;

    private Movie movie;
    private Serie serie;

    public SearchResultFragment(Movie movie) {
        super(R.layout.fragment_search_result);
        this.movie = movie;
    }

    public SearchResultFragment(Serie serie) {
        super(R.layout.fragment_search_result);
        this.serie = serie;
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

        searchResultImage.setClipToOutline(true);
        searchResultImage.setBackground(AppCompatResources.getDrawable(view.getContext(), R.drawable.media_card));

        searchResultContainer.setOnClickListener(v -> {
            if (movie != null) {
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("movieId", movie.getId());

                Interaction interaction = new Interaction();
                interaction.setMediaId(movie.getId());
                interaction.setMediaType("movie");
                interaction.setInteractionType("click");
                interaction.sendInteraction(view.getContext());

                startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), SerieDetailsActivity.class);
                intent.putExtra("serieId", serie.getId());

                Interaction interaction = new Interaction();
                interaction.setMediaId(serie.getId());
                interaction.setMediaType("serie");
                interaction.setInteractionType("click");
                interaction.sendInteraction(view.getContext());

                startActivity(intent);
            }
        });

        if (movie != null) {
            searchResultTitle.setText(movie.getTitle());

            MovieCategory movieCategory = MovieCategory.getMovieCategoryById(view.getContext(), movie.getCategoryId());
            searchResultGroup.setText(movieCategory.getName());

            Glide.with(view.getContext())
                    .load(movie.getPoster())
                    .into(searchResultImage);
        } else {
            searchResultTitle.setText(serie.getTitle());

            SerieCategory serieCategory = SerieCategory.getSerieCategoryById(serie.getCategoryId(), view.getContext());
            searchResultGroup.setText(serieCategory.getName());

            Glide.with(view.getContext())
                    .load(serie.getPoster())
                    .into(searchResultImage);
        }

        return view;
    }
}
