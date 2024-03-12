package com.nassimlnd.flixhub.Controller.Home.Fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments.MovieCategoryFragment;
import com.nassimlnd.flixhub.Controller.Media.MovieDetailsActivity;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It's the fragment where are shown the favorites categories and highlighted media
 * It's the first fragment that the logged user see when he opens the app
 */
public class HomeFragment extends Fragment {

    // Constants
    private final int AMOUNT_MOVIES_PER_CATEGORY = 10;
    private final String TAG = "HomeFragment";

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

        showHighlightedMedia(getContext());
        getContent();

        return view;
    }

    public void getContent() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Getting the shared preferences to get the user's favorites categories
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
            String favoritesCategories = sharedPreferences.getString("interests", "");
            try {
                JSONArray jsonArray = new JSONArray(favoritesCategories);
                // Setting the content of the view
                for (int i = 0; i < jsonArray.length(); i++) {
                    getMoviesByCategory(jsonArray.getString(i), getContext());
                }
            } catch (JSONException e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error while getting the favorites categories", Toast.LENGTH_SHORT).show());
            }

            // Hide the progress bar and show the content
            getActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            });
        });


    }

    public void showHighlightedMedia(Context ctx) {
        // Get a random movie
        Movie highlightedMedia = Movie.getSingleRandomMovie(ctx);

        // Set the content of the view
        highlightTitle.setText(highlightedMedia.getTitle());
        MovieCategory movieCategory = MovieCategory.getMovieCategoryById(ctx, highlightedMedia.getCategoryId());
        highlightGroupTitle.setText(movieCategory.getName());

        // Set the listener for the play button of the highlitghted media
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
            intent.putExtra("mediaId", highlightedMedia.getId());
            startActivity(intent);
        });

        // Loading the image of the highlighted media
        Glide.with(highlightImage.getContext())
                .load(highlightedMedia.getPoster())
                .placeholder(circularProgressDrawable)
                .into(highlightImage);
    }

    public void getMoviesByCategory(String category, Context ctx) {
        // Get the movies of the category
        MovieCategory movieCategory = MovieCategory.getMovieCategoryById(ctx, Integer.parseInt(category));
        ArrayList<Movie> moviesList = Movie.getMoviesByCategory(String.valueOf(movieCategory.getId()), ctx, AMOUNT_MOVIES_PER_CATEGORY);

        // Create the fragment for the category
        MovieCategoryFragment movieCategoryFragment = new MovieCategoryFragment(category, moviesList);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.categoryContainer, movieCategoryFragment).commit();
    }
}
