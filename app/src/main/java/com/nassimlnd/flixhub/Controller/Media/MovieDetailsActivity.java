package com.nassimlnd.flixhub.Controller.Media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Media.Fragments.MediaActorFragment;
import com.nassimlnd.flixhub.Controller.Media.Fragments.MediaTrailersFragment;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.Controller.Player.PlayerActivity;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieDetailsActivity extends AppCompatActivity {

    // View elements
    ProgressBar loadingSpinner;
    ImageView backBtn, mediaImage;
    Button playButton;
    ScrollView content;
    TextView mediaTitle, mediaDescription, mediaRating, mediaYear, mediaGroupTitle, trailerTitle;
    FlexboxLayout mediaRatingButton;

    // Data
    private Movie movie;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        loadingSpinner = findViewById(R.id.loading_spinner);
        content = findViewById(R.id.mediaContainer);
        mediaImage = findViewById(R.id.imageView);
        mediaTitle = findViewById(R.id.mediaTitle);
        mediaDescription = findViewById(R.id.mediaDescription);
        mediaGroupTitle = findViewById(R.id.mediaGroupTitle);
        mediaYear = findViewById(R.id.mediaYear);
        backBtn = findViewById(R.id.backButton);
        playButton = findViewById(R.id.playButton);
        trailerTitle = findViewById(R.id.trailerTitle);
        mediaRatingButton = findViewById(R.id.mediaRatingButton);

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        playButton.setOnClickListener(v -> {
            if (count == 0) {
                count++;
                Toast.makeText(this, R.string.media_play_warn, Toast.LENGTH_LONG).show();
            } else if (count == 1) {
                count++;
                Toast.makeText(this, R.string.media_play_warn_confirmation, Toast.LENGTH_LONG).show();
            } else if (count == 2) {
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("mediaUrl", movie.getUrl());
                intent.putExtra("mediaId", getIntent().getIntExtra("movieId", 0));

                sendInteraction();
                startActivity(intent);
            }
        });

        int mediaId = getIntent().getIntExtra("movieId", 0);

        if (mediaId != 0) {
            getMedia(mediaId);
        } else {
            finish();
        }
    }

    public void getMedia(int mediaId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(getMainLooper());

        executorService.execute(() -> {
            String result = APIClient.getMethodWithCookies("/movies/" + mediaId, getApplicationContext());
            handler.post(() -> {
                if (result != null) {
                    Log.d("MediaActivity", result);

                    try {
                        JSONObject mediaJson = new JSONObject(result);
                        movie = new Movie();

                        movie.setId(mediaJson.getInt("id"));
                        movie.setTitle(mediaJson.getString("title"));
                        movie.setPoster(mediaJson.getString("poster"));
                        movie.setTmdbId(mediaJson.getString("tmdbId"));
                        movie.setCategoryId(mediaJson.getInt("categoryId"));
                        movie.setStreamId(mediaJson.getString("streamId"));
                        movie.setUrl(mediaJson.getString("url"));

                        mediaTitle.setText(movie.getTitle().split(String.valueOf("\\("))[0].trim());

                        MovieCategory movieCategory = MovieCategory.getMovieCategoryById(getApplicationContext(), movie.getCategoryId());

                        mediaGroupTitle.setText(movieCategory.getName());

                        getMovieDetails(movie);

                        Glide.with(mediaImage.getContext())
                                .load(movie.getPoster())
                                .into(mediaImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        });
    }

    public void getMovieDetails(Movie movie) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(getMainLooper());
        String locale = Locale.getDefault().getLanguage();
        StringBuilder lang = new StringBuilder();
        String title = movie.getTitle();
        title = title.split(String.valueOf("\\("))[0].trim();

        try {
            switch (locale) {
                case "fr":
                    lang.append("fr-FR");
                    break;
                case "en":
                    lang.append("en-US");
                    break;
                default:
                    lang.append("fr-FR");
                    break;
            }

            String url = "https://api.themoviedb.org/3/movie/" + movie.getTmdbId() + "?api_key=bee04557bade921aab4537b991dfb6df&language=" + lang;

            executorService.execute(() -> {
                String result = APIClient.getMethodExternalAPI(url);
                handler.post(() -> {
                    try {
                        JSONObject movieJson = new JSONObject(result);

                        mediaDescription.setText(movieJson.getString("overview"));
                        String year = movieJson.getString("release_date").split("-")[0];
                        mediaYear.setText(year);

                        getMediaActors(movie.getTmdbId());
                        getMoviesTrailers(movie.getTmdbId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMediaActors(String movieId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(getMainLooper());

        executorService.execute(() -> {

            String url = "https://api.themoviedb.org/3/movie/" + movieId + "/casts?api_key=bee04557bade921aab4537b991dfb6df";
            String result = APIClient.getMethodExternalAPI(url);

            handler.post(() -> {
                Log.d("MediaActivity", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray actors = jsonObject.getJSONArray("cast");

                    for (int i = 0; i < actors.length(); i++) {
                        JSONObject actor = actors.getJSONObject(i);
                        String name = actor.getString("name");
                        String character = actor.getString("character");
                        String profilePath = actor.getString("profile_path");

                        HashMap<String, String> data = new HashMap<>();
                        data.put("name", name);
                        data.put("character", character);
                        data.put("profile_path", profilePath);

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.actorsContainer, new MediaActorFragment(data))
                                .commit();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void getMoviesTrailers(String movieId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(getMainLooper());

        executorService.execute(() -> {
            String locale = Locale.getDefault().getLanguage();
            StringBuilder lang = new StringBuilder();
            switch (locale) {
                case "fr":
                    lang.append("fr-FR");
                    break;
                case "en":
                    lang.append("en-US");
                    break;
                default:
                    lang.append("fr-FR");
                    break;
            }

            String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=bee04557bade921aab4537b991dfb6df&language=" + lang;
            String result = APIClient.getMethodExternalAPI(url);

            handler.post(() -> {
                Log.d("Trailers", result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray trailers = jsonObject.getJSONArray("results");

                    if (trailers.length() == 0) {
                        trailerTitle.setVisibility(View.GONE);
                        loadingSpinner.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (int i = 0; i < trailers.length(); i++) {
                        JSONObject trailer = trailers.getJSONObject(i);
                        String trailerTitle = trailer.getString("name");
                        String trailerDuration = trailer.getString("size");
                        String trailerImage = "https://img.youtube.com/vi/" + trailer.getString("key") + "/hqdefault.jpg";

                        MediaTrailersFragment mediaTrailersFragment = new MediaTrailersFragment(trailerTitle, trailerDuration, trailerImage);

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.trailersContainer, mediaTrailersFragment)
                                .commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                loadingSpinner.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            });
        });
    }

    public void sendInteraction() {
        // Get the profile from the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        int profileId = sharedPreferences.getInt("id", 0);

        if (profileId == 0) {
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("mediaId", String.valueOf(movie.getId()));
        data.put("profileId", String.valueOf(profileId));
        data.put("mediaType", "movie");
        data.put("interactionType", "view");

        // Send the interaction to the server
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                try {
                    String result = APIClient.postMethodWithCookies("/profile/" + profileId + "/interaction/", data, getApplicationContext());
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
