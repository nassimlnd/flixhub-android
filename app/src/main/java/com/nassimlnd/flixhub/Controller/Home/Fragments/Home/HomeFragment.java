package com.nassimlnd.flixhub.Controller.Home.Fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments.SerieCategoryFragment;
import com.nassimlnd.flixhub.Controller.Media.MovieDetailsActivity;
import com.nassimlnd.flixhub.Model.List;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.Model.Serie;
import com.nassimlnd.flixhub.Model.SerieCategory;
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
    private String tabSelected = "movies";

    private boolean state;
    // View elements
    ImageView highlightMovieImage, highlightSerieImage;
    TextView highlightMovieTitle, highlightMovieCategory, highlightSerieTitle, highlightSerieCategory, movieTab, serieTab;
    RelativeLayout content;
    ProgressBar progressBar;
    Button playMovieButton, listMovieButton, playSerieButton, listSerieButton;
    ImageView notificationButton;
    CircularProgressDrawable circularProgressDrawable;
    ScrollView moviesContent, seriesContent;

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
        highlightMovieImage = view.findViewById(R.id.highlight_movie_image);
        highlightMovieTitle = view.findViewById(R.id.highlight_movie_title);
        highlightMovieCategory = view.findViewById(R.id.highlight_movie_category);

        highlightSerieImage = view.findViewById(R.id.highlight_serie_image);
        highlightSerieTitle = view.findViewById(R.id.highlight_serie_title);
        highlightSerieCategory = view.findViewById(R.id.highlight_serie_category);

        playSerieButton = view.findViewById(R.id.highlight_serie_play_button);
        listSerieButton = view.findViewById(R.id.highlight_serie_list_button);

        playMovieButton = view.findViewById(R.id.highlight_movie_play_button);
        listMovieButton = view.findViewById(R.id.highlight_movie_list_button);

        content = view.findViewById(R.id.content);
        progressBar = view.findViewById(R.id.loading_spinner);
        notificationButton = view.findViewById(R.id.home_notification_button);

        movieTab = view.findViewById(R.id.homeTabMovies);
        serieTab = view.findViewById(R.id.homeTabSeries);

        moviesContent = view.findViewById(R.id.movies_content);
        seriesContent = view.findViewById(R.id.series_content);

        circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.primary));
        circularProgressDrawable.start();

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.selected_tab);
        movieTab.setBackground(drawable);

        // Set the listener for the tabs
        movieTab.setOnClickListener(v -> {
            if (!tabSelected.equals("movies")) {
                tabSelected = "movies";
                movieTab.setBackground(drawable);
                serieTab.setBackground(null);

                moviesContent.setVisibility(View.VISIBLE);
                seriesContent.setVisibility(View.GONE);
            }
        });

        serieTab.setOnClickListener(v -> {
            if (!tabSelected.equals("series")) {
                tabSelected = "series";
                serieTab.setBackground(drawable);
                movieTab.setBackground(null);

                moviesContent.setVisibility(View.GONE);
                seriesContent.setVisibility(View.VISIBLE);
            }
        });

        showHighlightedMedia(getContext());
        getContent();

        return view;
    }

    public void getContent() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Getting the shared preferences to get the user's favorites categories
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
            String favoritesMovieCategories = sharedPreferences.getString("movieInterests", "");
            String favoritesSerieCategories = sharedPreferences.getString("serieInterests", "");
            try {
                JSONArray favoritesMovieCategoriesJson = new JSONArray(favoritesMovieCategories);
                JSONArray favoritesSerieCategoriesJson = new JSONArray(favoritesSerieCategories);

                // Setting the content of the view
                for (int i = 0; i < favoritesMovieCategoriesJson.length(); i++) {
                    getMoviesByCategory(favoritesMovieCategoriesJson.getString(i), getContext());
                }

                for (int i = 0; i < favoritesSerieCategoriesJson.length(); i++) {
                    getSeriesByCategory(favoritesSerieCategoriesJson.getString(i), getContext());
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

        // Get a random serie
        Serie highlightedSerie = Serie.getRandomSerie(ctx);
        ArrayList<List> lists = List.getListByProfile(ctx);
        state = true;
        setButtonState(ctx, highlightedMedia);

        for (List list : lists) {
            if (highlightedMedia.getId() == list.getMovie().getId()) {
                listMovieButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                state = false;
                setButtonState(ctx, highlightedMedia);
                break;
            } else
                listMovieButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus, 0, 0, 0);
        }
        // Set the content of the view
        highlightMovieTitle.setText(highlightedMedia.getTitle());
        MovieCategory movieCategory = MovieCategory.getMovieCategoryById(ctx, highlightedMedia.getCategoryId());
        highlightMovieCategory.setText(movieCategory.getName());

        highlightSerieTitle.setText(highlightedSerie.getTitle());
        SerieCategory serieCategory = SerieCategory.getSerieCategoryById(highlightedSerie.getCategoryId(), ctx);
        highlightSerieCategory.setText(serieCategory.getName());

        // Set the listener for the play button of the highlitghted media
        playMovieButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
            intent.putExtra("movieId", highlightedMedia.getId());
            startActivity(intent);
        });


        // Loading the image of the highlighted media
        Glide.with(highlightMovieImage.getContext())
                .load(highlightedMedia.getPoster())
                .placeholder(circularProgressDrawable)
                .into(highlightMovieImage);

        Glide.with(highlightSerieImage.getContext())
                .load(highlightedSerie.getPoster())
                .placeholder(circularProgressDrawable)
                .into(highlightSerieImage);
    }

    public void getMoviesByCategory(String category, Context ctx) {
        // Get the movies of the category
        MovieCategory movieCategory = MovieCategory.getMovieCategoryById(ctx, Integer.parseInt(category));
        ArrayList<Movie> moviesList = Movie.getMoviesByCategory(String.valueOf(movieCategory.getId()), ctx, AMOUNT_MOVIES_PER_CATEGORY);

        // Create the fragment for the category
        MovieCategoryFragment movieCategoryFragment = new MovieCategoryFragment(movieCategory.getName(), moviesList);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.movieCategoryContainer, movieCategoryFragment).commit();
    }

    private void getSeriesByCategory(String string, Context context) {
        // Get the series of the category
        SerieCategory serieCategory = SerieCategory.getSerieCategoryById(Integer.parseInt(string), context);
        ArrayList<Serie> seriesList = Serie.getSeriesByCategory(context, serieCategory.getId(), AMOUNT_MOVIES_PER_CATEGORY);

        // Create the fragment for the category
        SerieCategoryFragment serieCategoryFragment = new SerieCategoryFragment(serieCategory, seriesList);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.serieCategoryContainer, serieCategoryFragment).commit();
    }
    public void setButtonState(Context ctx, Movie movie) {
        if (state) {
            listMovieButton.setOnClickListener(v -> {
                List.addMovie(ctx, movie);
                Toast.makeText(ctx, R.string.add_movie, Toast.LENGTH_LONG).show();
                listMovieButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                state = false;
                setButtonState(ctx, movie);
            });
        } else {
            listMovieButton.setOnClickListener(v -> {
                List.removeMovie(ctx, movie);
                Toast.makeText(ctx, R.string.remove_movie, Toast.LENGTH_LONG).show();
                listMovieButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus, 0, 0, 0);
                state = true;
                setButtonState(ctx, movie);
            });
        }

    }
}
