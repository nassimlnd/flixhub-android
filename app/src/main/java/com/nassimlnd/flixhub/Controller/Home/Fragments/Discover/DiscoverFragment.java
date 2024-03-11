package com.nassimlnd.flixhub.Controller.Home.Fragments.Discover;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Discover.Fragments.SearchResultFragment;
import com.nassimlnd.flixhub.Controller.Media.MovieDetailsActivity;
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

/**
 * It's the fragment where the user can discover new movies and series
 * It shows random movies and series and a search bar to search for a specific movie or serie
 */
public class DiscoverFragment extends Fragment {

    // View elements
    ScrollView mediaContainer, searchListLayout;
    EditText searchInput;
    FlexboxLayout randomContent;
    LinearLayout searchContent;
    ProgressBar loadingSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        mediaContainer = view.findViewById(R.id.randomContentLayout);
        searchInput = view.findViewById(R.id.searchInput);
        searchListLayout = view.findViewById(R.id.searchListLayout);
        searchContent = view.findViewById(R.id.searchListContent);
        randomContent = view.findViewById(R.id.randomContent);
        loadingSpinner = view.findViewById(R.id.loading_spinner);

        showRandomMovies();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchInput.getText().toString().length() > 2) {
                    showSearchedMovies();
                }
            }
        });

        return view;
    }

    public void showRandomMovies() {
        ArrayList<Movie> movies = Movie.getRandomMovies(getContext(), 20);

        for (Movie movie : movies) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(450, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 24);

            imageView.setLayoutParams(layoutParams);
            imageView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.media_card));
            imageView.setClipToOutline(true);

            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("mediaId", movie.getId());

                Interaction interaction = new Interaction();
                interaction.setMediaId(movie.getId());
                interaction.setMediaType("movie");
                interaction.setInteractionType("click");
                interaction.sendInteraction(getContext());

                startActivity(intent);
            });

            Glide.with(imageView.getContext())
                    .load(movie.getPoster())
                    .transition(withCrossFade())
                    .into(imageView);

            randomContent.addView(imageView);
        }

        loadingSpinner.setVisibility(View.GONE);
        mediaContainer.setVisibility(View.VISIBLE);
    }

    public void showSearchedMovies() {
        String input = searchInput.getText().toString();
        ArrayList<Movie> movies = Movie.getSearchedMovies(input, getContext());
        mediaContainer.setVisibility(View.GONE);
        for (Movie movie : movies) {
            SearchResultFragment searchResultFragment = new SearchResultFragment(movie);

            searchContent.removeAllViews();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.searchListContent, searchResultFragment)
                    .commit();
        }
        searchListLayout.setVisibility(View.VISIBLE);
    }


}
