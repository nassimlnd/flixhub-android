package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.widget.Toast;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It's the class that represents a movie.
 * It extends the Media class.
 * It contains methods to fetch movies from the server.
 */
public class Movie extends Media {

    public Movie() {
        super();
    }

    public Movie(int id, String title, String tvg_id, String tvg_name, String tvg_logo, String group_title, String url, Date createdAt, Date updatedAt) {
        super(id, title, tvg_id, tvg_name, tvg_logo, group_title, url, createdAt, updatedAt);
    }

    public static ArrayList<Media> getRandomMovies(Context ctx, int amount) {
        ArrayList<Media> medias = new ArrayList<>();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.callGetMethodWithCookies("/movies/random/" + amount, ctx);
                try {
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

                        medias.add(media);
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

        return medias;
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
                            movie.setTvg_name(movieJson.getString("tvgName"));
                            movie.setGroup_title(movieJson.getString("groupTitle"));
                            movie.setUrl(movieJson.getString("url"));
                            movie.setTvg_logo(movieJson.getString("tvgLogo"));

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
                    movie.setTvg_name(jsonMovie.getString("tvgName"));
                    movie.setTvg_logo(jsonMovie.getString("tvgLogo"));
                    movie.setGroup_title(jsonMovie.getString("groupTitle"));
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
                        movie.setTvg_name(jsonMovie.getString("tvgName"));
                        movie.setTvg_logo(jsonMovie.getString("tvgLogo"));
                        movie.setGroup_title(jsonMovie.getString("groupTitle"));
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
}
