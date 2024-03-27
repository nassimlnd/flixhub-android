package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

    /**
     * Get random movies from the server
     * @param ctx the context
     * @return ArrayList<Movie> movies
     */
    public static ArrayList<Movie> getRandomMovies(Context ctx, int amount) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.getMethodWithCookies("/movies/random/" + amount, ctx);
                try {
                    JSONArray moviesArray = new JSONArray(result);

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
                    String result = APIClient.getMethodWithCookies("/movies/search/" + inputEncoded, ctx);
                    try {
                        JSONArray moviesArray = new JSONArray(result);

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

    /**
     * Get a single random movie from the server
     * @param ctx the context
     * @return Movie movie
     */
    public static Movie getSingleRandomMovie(Context ctx) {
        Movie movie = new Movie();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.getMethodWithCookies("/movies/random", ctx);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonMovie = jsonArray.getJSONObject(0);

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

    /**
     * Get the movies by category
     * @param category the category of the movies
     * @param ctx the context
     * @param amount the amount of movies to fetch
     * @return ArrayList<Movie> movies
     */
    public static ArrayList<Movie> getMoviesByCategory(String category, Context ctx, int amount) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            String param = "/movies/category/" + category + "/movies/" + amount;

            ExecutorService executor =
                    Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);
            executor.execute(() -> {
                String result = APIClient.getMethodWithCookies(param, ctx);

                try {
                    JSONArray moviesJson = new JSONArray(result);

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

    /**
     * Get the movie history of the user by its profile
     * @param ctx the context
     * @return ArrayList<Movie> movies
     */
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
                String result = APIClient.getMethodWithCookies(url, ctx);
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

    /**
     * Get the movie from the server by its id
     * @param movieId the id of the movie
     * @param ctx the context
     * @return Movie movie
     */
    public static Movie getMovieById(int movieId, Context ctx) {
        Movie movie = new Movie();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.getMethodWithCookies("/movies/" + movieId, ctx);
                try {
                    JSONObject mediaObject = new JSONObject(result);

                    movie.setId(mediaObject.getInt("id"));
                    movie.setTitle(mediaObject.getString("title"));
                    movie.setCategoryId(mediaObject.getInt("categoryId"));
                    movie.setPoster(mediaObject.getString("poster"));
                    movie.setStreamId(mediaObject.getString("streamId"));
                    movie.setTmdbId(mediaObject.getString("tmdbId"));
                    movie.setUrl(mediaObject.getString("url"));
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

    /**
     * Get the details of the movie from TMDB API
     * @return HashMap<String, String> movieDetails
     */
    public HashMap<String, String> getMovieDetails() {
        HashMap<String, String> movieDetails = new HashMap<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            executor.execute(() -> {
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
                        lang.append("en-US");
                        break;
                }

                String url = "https://api.themoviedb.org/3/movie/" + this.getTmdbId() + "?api_key=bee04557bade921aab4537b991dfb6df&language=" + lang;
                String result = APIClient.getMethodExternalAPI(url);
                try {
                    JSONObject movieObject = new JSONObject(result);

                    movieDetails.put("overview", movieObject.getString("overview"));
                    movieDetails.put("release_date", movieObject.getString("release_date"));

                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return movieDetails;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the cast of the movie from TMDB API
     * @return HashMap<String, HashMap<String, String>> movieCast
     */
    public HashMap<String, HashMap<String, String>> getMovieCast() {
        HashMap<String, HashMap<String, String>> movieCast = new HashMap<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            executor.execute(() -> {
                String url = "https://api.themoviedb.org/3/movie/" + this.getTmdbId() + "/casts?api_key=bee04557bade921aab4537b991dfb6df";
                String result = APIClient.getMethodExternalAPI(url);

                try {
                    JSONObject castObject = new JSONObject(result);
                    JSONArray castArray = castObject.getJSONArray("cast");

                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject actor = castArray.getJSONObject(i);
                        HashMap<String, String> actorDetails = new HashMap<>();

                        actorDetails.put("name", actor.getString("name"));
                        actorDetails.put("character", actor.getString("character"));
                        actorDetails.put("profile_path", actor.getString("profile_path"));

                        movieCast.put(actor.getString("name"), actorDetails);
                    }

                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return movieCast;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the trailers of the movie from TMDB API
     * @return ArrayList<HashMap<String, String>> movieTrailers
     */
    public ArrayList<HashMap<String, String>> getMovieTrailers() {
        ArrayList<HashMap<String, String>> movieTrailers = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        try {
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

                String url = "https://api.themoviedb.org/3/movie/" + this.getTmdbId() + "/videos?api_key=bee04557bade921aab4537b991dfb6df&language=" + lang;
                String result = APIClient.getMethodExternalAPI(url);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray trailers = jsonObject.getJSONArray("results");

                    if (trailers.length() > 0) {
                        for (int i = 0; i < trailers.length(); i++) {
                            JSONObject trailer = trailers.getJSONObject(i);
                            HashMap<String, String> trailerDetails = new HashMap<>();

                            trailerDetails.put("name", trailer.getString("name"));
                            trailerDetails.put("key", trailer.getString("key"));
                            trailerDetails.put("size", trailer.getString("size"));
                            trailerDetails.put("poster", "https://img.youtube.com/vi/" + trailer.getString("key") + "/hqdefault.jpg");

                            movieTrailers.add(trailerDetails);
                        }
                        latch.countDown();
                    } else {
                        latch.countDown();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return movieTrailers;
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
