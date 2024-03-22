package com.nassimlnd.flixhub.Controller.Media;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.Model.Episode;
import com.nassimlnd.flixhub.Model.Serie;
import com.nassimlnd.flixhub.Model.SerieCategory;
import com.nassimlnd.flixhub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "SerieDetailsActivity";
    // View elements
    ProgressBar progressBar;
    ImageView backBtn, serieImage;
    Button playButton;
    ScrollView content;
    TextView serieTitle, serieDescription, serieRating, serieYear, serieCategory, trailerTitle;
    FlexboxLayout serieRatingButton;
    Spinner seasonSpinner;

    // Data
    private Serie serie;
    private int count = 0;
    private int nbSeasons = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        backBtn = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.loading_spinner);
        content = findViewById(R.id.mediaContainer);
        serieImage = findViewById(R.id.imageView);
        serieTitle = findViewById(R.id.mediaTitle);
        serieDescription = findViewById(R.id.mediaDescription);
        serieRating = findViewById(R.id.mediaRating);
        serieYear = findViewById(R.id.mediaYear);
        serieCategory = findViewById(R.id.mediaGroupTitle);
        serieRatingButton = findViewById(R.id.mediaRatingButton);
        seasonSpinner = findViewById(R.id.seasonSpinner);

        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        int serieId = getIntent().getIntExtra("serieId", 0);

        if (serieId != 0) {
            getSerie(serieId, getApplicationContext());
        } else {
            finish();
        }
    }

    public void getSerie(int serieId, Context ctx) {
        serie = Serie.getSerieById(ctx, serieId);

        if (serie != null) {
            serieTitle.setText(serie.getTitle());
            SerieCategory serieCat = SerieCategory.getSerieCategoryById(serie.getCategoryId(), ctx);
            serieCategory.setText(serieCat.getName());

            getSerieDetails(serie);

            ArrayList<Episode> episodes = serie.getEpisodes();

            for (Episode episode : episodes) {
                if (episode.getSeasonNumber() > nbSeasons) {
                    nbSeasons = episode.getSeasonNumber();
                }
            }

            ArrayList<String> seasons = new ArrayList<>();

            for (int i = 1; i <= nbSeasons; i++) {
                seasons.add("Saison " + i);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, seasons);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            seasonSpinner.setAdapter(adapter);

            seasonSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemSelected: " + position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            Glide.with(serieImage.getContext())
                    .load(serie.getPoster())
                    .into(serieImage);

            progressBar.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    public void getSerieDetails(Serie serie) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        String locale = Locale.getDefault().getLanguage();
        StringBuilder lang = new StringBuilder();

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

            String url = "https://api.themoviedb.org/3/tv/" + serie.getTmdbId() + "?api_key=bee04557bade921aab4537b991dfb6df&language=" + lang;

            executor.execute(() -> {
                String result = APIClient.getMethodExternalAPI(url);

                try {
                    JSONObject serieJSON = new JSONObject(result);

                    serieDescription.setText(serieJSON.getString("overview"));
                    String year = serieJSON.getString("first_air_date").split("-")[0];
                    serieYear.setText(year);

                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
