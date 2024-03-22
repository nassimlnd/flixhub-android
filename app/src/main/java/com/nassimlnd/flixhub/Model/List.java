package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class List {
    private int id;
    private Movie movie;
    private int profileId;

    public List(Movie movie, int profileId) {
        this.id = id;
        this.movie = movie;
        this.profileId = profileId;
    }

    public static ArrayList<List> getListByProfileId(Context ctx) {
        ArrayList<List> List = new ArrayList<>();
        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("profile", Context.MODE_PRIVATE);
            int profileId = sharedPreferences.getInt("id", 0);
            String param = "/profile/" + profileId + "/list";
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);
            executor.execute(() -> {
                String result = APIClient.getMethodWithCookies(param, ctx);
                try {
                    JSONArray resultArray = new JSONArray(result);
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject ListJson = resultArray.getJSONObject(i);
                        int movieId = ListJson.getInt("movieId");
                        int profile = ListJson.getInt("profileId");
                    }
                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            });
            latch.await();
            return List;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
}
