package com.nassimlnd.flixhub.Controller.Home.Fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments.CategoryFragment;
import com.nassimlnd.flixhub.Controller.Media.MediaActivity;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    ImageView imageView;
    TextView highlightTitle;
    TextView highlightGroupTitle;
    ScrollView content;
    ProgressBar progressBar;
    Button playButton;
    Button downloadButton;
    ImageView searchButton;
    ImageView notificationButton;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = view.findViewById(R.id.imageView);
        highlightTitle = view.findViewById(R.id.highlight_title);
        highlightGroupTitle = view.findViewById(R.id.highlight_group_title);
        content = view.findViewById(R.id.content);
        progressBar = view.findViewById(R.id.loading_spinner);
        playButton = view.findViewById(R.id.highlight_play_button);
        downloadButton = view.findViewById(R.id.highlight_download_button);
        searchButton = view.findViewById(R.id.home_search_button);
        notificationButton = view.findViewById(R.id.home_notification_button);

        showHighlightedMedia(view.getContext());

        return view;
    }

    public void showHighlightedMedia(Context ctx) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(ctx);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(ctx, R.color.primary));
        circularProgressDrawable.start();

        Movie highlightedMedia = Movie.getSingleRandomMovie(ctx);

        highlightTitle.setText(highlightedMedia.getTvg_name());
        highlightGroupTitle.setText(highlightedMedia.getGroup_title());

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MediaActivity.class);
            intent.putExtra("mediaId", highlightedMedia.getId());
            startActivity(intent);
        });

        Glide.with(imageView.getContext())
                .load(highlightedMedia.getTvg_logo())
                .placeholder(circularProgressDrawable)
                .into(imageView);

        getMoviesByCategory("FILMS RÉCEMMENT AJOUTÉS", ctx);
        getMoviesByCategory("MANGAS", ctx);
        getMoviesByCategory("DOCUMENTAIRES | EMISSION TV", ctx);
        getMoviesByCategory("ANIMATION | FAMILIALE | ENFANTS", ctx);

        progressBar.setVisibility(ProgressBar.GONE);
        content.setVisibility(ScrollView.VISIBLE);
    }

    public void getMoviesByCategory(String category, Context ctx) {
        try {
            String categoryFormatted = URLEncoder.encode(category, "UTF-8");
            String param = "/movies/groups/" + categoryFormatted + "/10";

            ExecutorService executor =
                    Executors.newSingleThreadExecutor();
            Handler handler = new
                    Handler(Looper.getMainLooper());
            executor.execute(() -> {
                String result = APIClient.callGetMethodWithCookies(param, ctx);
                handler.post(() -> {
                    insertCategoryMedia(category, result);
                });
            });
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertCategoryMedia(String categoryName, String result) {
        ArrayList<Media> moviesList = new ArrayList<>();

        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONArray movies = jsonResult.getJSONArray("movies");

            for (int i = 0; i < movies.length(); i++) {
                JSONObject jsonMovie = movies.getJSONObject(i);
                Media movie = new Media();
                movie.setId(Integer.parseInt(jsonMovie.getString("id")));
                movie.setTitle(jsonMovie.getString("title"));
                //movie.setTvg_id(jsonMovie.getString("tvgId"));
                movie.setTvg_name(jsonMovie.getString("tvgName"));
                movie.setTvg_logo(jsonMovie.getString("tvgLogo"));
                movie.setGroup_title(jsonMovie.getString("groupTitle"));
                movie.setUrl(jsonMovie.getString("url"));

                moviesList.add(movie);
            }

            CategoryFragment categoryFragment = new CategoryFragment(categoryName, moviesList);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.categoryContainer, categoryFragment)
                    .commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
