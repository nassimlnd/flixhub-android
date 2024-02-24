package com.nassimlnd.flixhub.Controller.Home.Fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments.CategoryFragment;
import com.nassimlnd.flixhub.Controller.Media.MediaActivity;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

/**
 * It's the fragment where are shown the favorites categories and highlighted media
 * It's the first fragment that the logged user see when he opens the app
 */
public class HomeFragment extends Fragment {

    // Constants
    private final int AMOUNT_MOVIES_PER_CATEGORY = 10;

    // View elements
    ImageView highlightImage;
    TextView highlightTitle, highlightGroupTitle;
    ScrollView content;
    ProgressBar progressBar;
    Button playButton, downloadButton;
    ImageView searchButton, notificationButton;
    CircularProgressDrawable circularProgressDrawable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Setting the view
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Gettings all the elements from the view
        highlightImage = view.findViewById(R.id.highlight_image);
        highlightTitle = view.findViewById(R.id.highlight_title);
        highlightGroupTitle = view.findViewById(R.id.highlight_group_title);
        content = view.findViewById(R.id.content);
        progressBar = view.findViewById(R.id.loading_spinner);
        playButton = view.findViewById(R.id.highlight_play_button);
        downloadButton = view.findViewById(R.id.highlight_download_button);
        searchButton = view.findViewById(R.id.home_search_button);
        notificationButton = view.findViewById(R.id.home_notification_button);
        circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.primary));
        circularProgressDrawable.start();

        // Setting the content of the view
        showHighlightedMedia(view.getContext());

        getMoviesByCategory("FILMS RÉCEMMENT AJOUTÉS", getContext());
        getMoviesByCategory("MANGAS", getContext());
        getMoviesByCategory("DOCUMENTAIRES | EMISSION TV", getContext());
        getMoviesByCategory("ANIMATION | FAMILIALE | ENFANTS", getContext());

        // Hide the progress bar and show the content
        progressBar.setVisibility(ProgressBar.GONE);
        content.setVisibility(ScrollView.VISIBLE);

        return view;
    }

    public void showHighlightedMedia(Context ctx) {
        // Get a random movie
        Movie highlightedMedia = Movie.getSingleRandomMovie(ctx);

        // Set the content of the view
        highlightTitle.setText(highlightedMedia.getTvg_name());
        highlightGroupTitle.setText(highlightedMedia.getGroup_title());

        // Set the listener for the play button of the highlitghted media
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MediaActivity.class);
            intent.putExtra("mediaId", highlightedMedia.getId());
            startActivity(intent);
        });

        // Loading the image of the highlighted media
        Glide.with(highlightImage.getContext()).load(highlightedMedia.getTvg_logo()).placeholder(circularProgressDrawable).into(highlightImage);
    }

    public void getMoviesByCategory(String category, Context ctx) {
        // Get the movies of the category
        ArrayList<Movie> moviesList = Movie.getMoviesByCategory(category, ctx, AMOUNT_MOVIES_PER_CATEGORY);

        // Create the fragment for the category
        CategoryFragment categoryFragment = new CategoryFragment(category, moviesList);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.categoryContainer, categoryFragment).commit();
    }
}
