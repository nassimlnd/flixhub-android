package com.nassimlnd.flixhub.Model;

import android.content.Context;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerieCategory {

    // Attributes
    private int id;
    private String name;

    // Constructor
    public SerieCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SerieCategory() {

    }

    // Back-end methods

    public static SerieCategory getSerieCategoryById(int id, Context ctx) {
        SerieCategory serieCategory = new SerieCategory();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.getMethodWithCookies("/series/category/" + id, ctx);
                try {
                    JSONObject serieCategoryObject = new JSONObject(result);

                    serieCategory.setId(serieCategoryObject.getInt("id"));
                    serieCategory.setName(serieCategoryObject.getString("name"));

                    latch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return serieCategory;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
