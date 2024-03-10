package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It's the class that represents a movie.
 * It extends the Media class.
 * It contains methods to fetch movies from the server.
 */
public class Movie {

    // Constants
    private static final String TAG = "Movie";

    // Attributes

    private int id;
    private String title;
    private String streamId;
    private String poster;
    private int categoryId;
    private String url;
    private String tmdbId;

    // Constructor
    public Movie() {
    }

    public Movie(int id, String title, String streamId, String poster, int categoryId, String url, String tmdbId) {
        this.id = id;
        this.title = title;
        this.streamId = streamId;
        this.poster = poster;
        this.categoryId = categoryId;
        this.url = url;
        this.tmdbId = tmdbId;
    }

    // Back-end methods
    public static ArrayList<Movie> getRandomMovies(Context ctx, int amount) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.callGetMethodWithCookies("/movies/random/" + amount, ctx);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray moviesArray = jsonObject.getJSONArray("movies");

                    for (int i = 0; i < moviesArray.length(); i++) {
                        JSONObject movieObject = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setId(movieObject.getInt("id"));
                        movie.setTitle(movieObject.getString("title"));
                        movie.setCategoryId(movieObject.getInt("categoryId"));
                        movie.setPoster(movieObject.getString("poster"));
                        movie.setStreamId(movieObject.getString("streamId"));
                        movie.setUrl(movieObject.getString("url"));
                        movie.setTmdbId(movieObject.getString("tmdbId"));

                        movies.add(movie);
                    }
                    latch.countDown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return movies;
    }

    public static ArrayList<Movie> getSearchedMovies(String input, Context ctx) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                try {
                    String inputEncoded = URLEncoder.encode(input, "UTF-8");
                    String result = APIClient.callGetMethodWithCookies("/movies/search/" + inputEncoded, ctx);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray moviesArray = jsonObject.getJSONArray("movies");

                        for (int i = 0; i < moviesArray.length(); i++) {
                            JSONObject movieJson = moviesArray.getJSONObject(i);
                            Movie movie = new Movie();

                            movie.setId(movieJson.getInt("id"));
                            movie.setTitle(movieJson.getString("title"));
                            movie.setCategoryId(movieJson.getInt("categoryId"));
                            movie.setPoster(movieJson.getString("poster"));
                            movie.setStreamId(movieJson.getString("streamId"));
                            movie.setUrl(movieJson.getString("url"));
                            movie.setTmdbId(movieJson.getString("tmdbId"));

                            movies.add(movie);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    latch.countDown();
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    public static Movie getSingleRandomMovie(Context ctx) {
        Movie movie = new Movie();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.callGetMethodWithCookies("/movies/random", ctx);
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray movies = jsonResult.getJSONArray("movie");
                    JSONObject jsonMovie = movies.getJSONObject(0);

                    movie.setId(Integer.parseInt(jsonMovie.getString("id")));
                    movie.setTitle(jsonMovie.getString("title"));
                    movie.setCategoryId(jsonMovie.getInt("categoryId"));
                    movie.setPoster(jsonMovie.getString("poster"));
                    movie.setStreamId(jsonMovie.getString("streamId"));
                    movie.setUrl(jsonMovie.getString("url"));

                    latch.countDown();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return movie;
    }

    public static ArrayList<Movie> getMoviesByCategory(String category, Context ctx, int amount) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            String categoryFormatted = URLEncoder.encode(category, "UTF-8");
            String param = "/movies/groups/" + categoryFormatted + "/" + amount;

            ExecutorService executor =
                    Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);
            executor.execute(() -> {
                String result = APIClient.callGetMethodWithCookies(param, ctx);

                try {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray moviesJson = jsonResult.getJSONArray("movies");

                    for (int i = 0; i < moviesJson.length(); i++) {
                        JSONObject jsonMovie = moviesJson.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setId(Integer.parseInt(jsonMovie.getString("id")));
                        movie.setTitle(jsonMovie.getString("title"));
                        movie.setCategoryId(jsonMovie.getInt("categoryId"));
                        movie.setPoster(jsonMovie.getString("poster"));
                        movie.setStreamId(jsonMovie.getString("streamId"));
                        movie.setTmdbId(jsonMovie.getString("tmdbId"));
                        movie.setUrl(jsonMovie.getString("url"));

                        movies.add(movie);
                    }
                } catch (JSONException e) {
                    Toast.makeText(ctx, "An error occurred while fetching movies", Toast.LENGTH_SHORT).show();
                }
                latch.countDown();
            });
            latch.await();
            return movies;
        } catch (Exception e) {
            Toast.makeText(ctx, "An error occurred while fetching movies", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public static ArrayList<Movie> getMovieHistoryByProfile(Context ctx) {
        // Get the profile id from the shared preferences
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("profile", Context.MODE_PRIVATE);
        int profileId = sharedPreferences.getInt("id", 0);
        String mediaType = "movie";
        String interactionType = "view";

        if (profileId == 0) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            String url = "/profile/" + profileId + "/interaction/" + interactionType + "/" + mediaType + "/";
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.callGetMethodWithCookies(url, ctx);
                try {
                    JSONArray resultArray = new JSONArray(result);
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject interactionObject = resultArray.getJSONObject(i);
                        int mediaId = interactionObject.getInt("mediaId");

                        Movie movie = getMovieById(mediaId, ctx);
                        movies.add(movie);
                    }
                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Movie getMovieById(int movieId, Context ctx) {
        Movie movie = new Movie();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.callGetMethodWithCookies("/movies/" + movieId, ctx);
                try {
                    JSONObject mediaObject = new JSONObject(result);
                    JSONObject mediaJson = mediaObject.getJSONObject("movie");
                    movie.setId(mediaJson.getInt("id"));
                    movie.setTitle(mediaJson.getString("title"));
                    movie.setCategoryId(mediaJson.getInt("categoryId"));
                    movie.setPoster(mediaJson.getString("poster"));
                    movie.setStreamId(mediaJson.getString("streamId"));
                    movie.setTmdbId(mediaJson.getString("tmdbId"));
                    movie.setUrl(mediaJson.getString("url"));
                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            latch.await();
            return movie;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }
}
