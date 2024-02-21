package com.nassimlnd.flixhub.Home.Fragments.Discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Media.MediaActivity;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Network.APIClient;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscoverFragment extends Fragment {

    ScrollView mediaContainer;
    ScrollView searchListLayout;
    ArrayList<Media> medias;
    EditText searchInput;
    FlexboxLayout randomContent;
    LinearLayout searchContent;

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

        getRandomMovies();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchInput.getText().toString().length() > 2) {
                    getSearchedMovies();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void getRandomMovies() {
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String result = APIClient.callGetMethodWithCookies("/movies/random/20", getContext());
            handler.post(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray moviesArray = jsonObject.getJSONArray("movies");
                    medias = new ArrayList<>();

                    for (int i = 0; i < moviesArray.length(); i++) {
                        JSONObject movie = moviesArray.getJSONObject(i);

                        Media media = new Media();
                        media.setId(movie.getInt("id"));
                        media.setTitle(movie.getString("title"));
                        media.setTvg_name(movie.getString("tvgName"));
                        media.setGroup_title(movie.getString("groupTitle"));
                        media.setUrl(movie.getString("url"));
                        media.setTvg_logo(movie.getString("tvgLogo"));

                        medias.add(media);

                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.WRAP_CONTENT));
                        imageView.setPadding(0, 0, 0, 24);

                        imageView.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), MediaActivity.class);
                            intent.putExtra("mediaId", media.getId());

                            startActivity(intent);
                        });

                        Glide.with(imageView.getContext())
                                .load(media.getTvg_logo())
                                .into(imageView);

                        randomContent.addView(imageView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void getSearchedMovies() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            try {
                String input = searchInput.getText().toString();
                input = URLEncoder.encode(input, "UTF-8");
                String result = APIClient.callGetMethodWithCookies("/movies/search/" + input, getContext());
                handler.post(() -> {
                    try {
                        Log.d("SEARCH", result);
                        mediaContainer.setVisibility(View.GONE);

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray moviesArray = jsonObject.getJSONArray("movies");

                        for (int i = 0; i < moviesArray.length(); i++) {
                            JSONObject movie = moviesArray.getJSONObject(i);
                            Media media = new Media();

                            media.setId(movie.getInt("id"));
                            media.setTitle(movie.getString("title"));
                            media.setTvg_name(movie.getString("tvgName"));
                            media.setGroup_title(movie.getString("groupTitle"));
                            media.setUrl(movie.getString("url"));
                            media.setTvg_logo(movie.getString("tvgLogo"));

                            SearchResultFragment searchResultFragment = new SearchResultFragment(media);

                            searchContent.removeAllViews();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .add(R.id.searchListContent, searchResultFragment)
                                    .commit();
                        }

                        searchListLayout.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
