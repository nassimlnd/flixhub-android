package com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Controller.Media.MovieDetailsActivity;
import com.nassimlnd.flixhub.Controller.Media.SerieDetailsActivity;
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.Serie;
import com.nassimlnd.flixhub.R;

public class MediaFragment extends Fragment {

    RelativeLayout mediaLayout;
    ImageView mediaImage;
    private Movie movie;
    private Serie serie;

    public MediaFragment(Movie movie) {
        super(R.layout.fragment_media);
        this.movie = movie;
    }

    public MediaFragment(Serie serie) {
        super(R.layout.fragment_media);
        this.serie = serie;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        mediaImage = view.findViewById(R.id.imageView);
        mediaLayout = view.findViewById(R.id.mediaContainer);

        mediaImage.setClipToOutline(true);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        mediaLayout.setOnClickListener(v -> {
            if (movie != null) {
                Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
                intent.putExtra("movieId", movie.getId());

                Interaction interaction = new Interaction();
                interaction.setMediaId(movie.getId());
                interaction.setMediaType("movie");
                interaction.setInteractionType("click");
                interaction.sendInteraction(view.getContext());

                startActivity(intent);
            } else {
                Intent intent = new Intent(view.getContext(), SerieDetailsActivity.class);
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
            if (movie.getPoster().equals("https://image.tmdb.org/t/p/w600_and_h900_bestv2")) {
                movie.setPoster("");
            }

            Glide.with(mediaImage.getContext())
                    .load(movie.getPoster())
                    .transition(withCrossFade())
                    .error(R.drawable.image)
                    .into(mediaImage);
        } else {
            if (serie.getPoster().equals("https://image.tmdb.org/t/p/w600_and_h900_bestv2")) {
                serie.setPoster("");
            }

            Glide.with(mediaImage.getContext())
                    .load(serie.getPoster())
                    .transition(withCrossFade())
                    .error(R.drawable.image)
                    .into(mediaImage);
        }


        return view;
    }
}
