package com.nassimlnd.flixhub.Model;

import android.content.Context;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serie {

    private int id;
    private String title;
    private String poster;
    private String serieId;
    private int categoryId;
    private int tmdbId;
    private ArrayList<Episode> episodes;

    public Serie(int id, String title, String poster, String serieId, int categoryId, int tmdbId) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.serieId = serieId;
        this.categoryId = categoryId;
        this.tmdbId = tmdbId;
    }

    public Serie() {

    }

    // Back-end methods

    public static Serie getSerieById(Context ctx, int serieId) {
        Serie serie = new Serie();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                String result = APIClient.getMethodWithCookies("/series/" + serieId, ctx);
                try {
                    JSONObject json = new JSONObject(result);
                    JSONObject serieJSON = json.getJSONObject("serie");

                    serie.setId(serieJSON.getInt("id"));
                    serie.setTitle(serieJSON.getString("title"));
                    serie.setPoster(serieJSON.getString("poster"));
                    serie.setSerieId(serieJSON.getString("serieId"));
                    serie.setCategoryId(serieJSON.getInt("categoryId"));
                    serie.setTmdbId(serieJSON.getInt("tmdbId"));

                    ArrayList<Episode> episodes = new ArrayList<>();
                    JSONArray episodesJSON = json.getJSONArray("episodes");

                    for (int i = 0; i < episodesJSON.length(); i++) {
                        JSONObject episodeJSON = episodesJSON.getJSONObject(i);
                        Episode episode = new Episode();
                        episode.setId(episodeJSON.getInt("id"));
                        episode.setTitle(episodeJSON.getString("title"));
                        episode.setEpisodeNumber(episodeJSON.getInt("episodeNum"));
                        episode.setSeasonNumber(episodeJSON.getInt("seasonNumber"));
                        episode.setSerieId(episodeJSON.getInt("serieId"));
                        episode.setUrl(episodeJSON.getString("url"));
                        episode.setPoster(episodeJSON.getString("poster"));
                        episodes.add(episode);
                    }

                    serie.setEpisodes(episodes);

                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return serie;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Serie> getAllSeries(Context ctx) {
        ArrayList<Serie> series = new ArrayList<>();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.getMethodWithCookies("/series", ctx);
                try {
                    JSONArray json = new JSONArray(result);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject serieJSON = json.getJSONObject(i);
                        Serie serie = new Serie();
                        serie.setId(serieJSON.getInt("id"));
                        serie.setTitle(serieJSON.getString("title"));
                        serie.setPoster(serieJSON.getString("poster"));
                        serie.setSerieId(serieJSON.getString("serieId"));
                        serie.setCategoryId(serieJSON.getInt("categoryId"));
                        serie.setTmdbId(serieJSON.getInt("tmdbId"));
                        series.add(serie);
                    }
                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return series;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Serie> getSeriesByCategory(Context ctx, int categoryId, int amount) {
        ArrayList<Serie> series = new ArrayList<>();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.getMethodWithCookies("/series/category/" + categoryId + "/" + amount, ctx);
                try {
                    JSONArray json = new JSONArray(result);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject serieJSON = json.getJSONObject(i);
                        Serie serie = new Serie();
                        serie.setId(serieJSON.getInt("id"));
                        serie.setTitle(serieJSON.getString("title"));
                        serie.setPoster(serieJSON.getString("poster"));
                        serie.setSerieId(serieJSON.getString("serieId"));
                        serie.setCategoryId(serieJSON.getInt("categoryId"));
                        serie.setTmdbId(serieJSON.getInt("tmdbId"));
                        series.add(serie);
                    }
                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return series;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Serie> getSearchSeries(Context ctx, String query) {
        return null;
    }

    public static Serie getRandomSerie(Context ctx) {
        Serie serie = new Serie();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.getMethodWithCookies("/series/random", ctx);
                try {
                    JSONObject serieJSON = new JSONObject(result);
                    serie.setId(serieJSON.getInt("id"));
                    serie.setTitle(serieJSON.getString("title"));
                    serie.setPoster(serieJSON.getString("poster"));
                    serie.setSerieId(serieJSON.getString("serieId"));
                    serie.setCategoryId(serieJSON.getInt("categoryId"));
                    serie.setTmdbId(serieJSON.getInt("tmdbId"));
                    countDownLatch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();
            });

            countDownLatch.await();
            return serie;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSerieId() {
        return serieId;
    }

    public void setSerieId(String serieId) {
        this.serieId = serieId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }
}
