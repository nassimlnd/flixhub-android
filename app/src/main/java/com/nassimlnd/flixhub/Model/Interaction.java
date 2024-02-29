package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Interaction {

    private int id;
    private int profileId;
    private int mediaId;
    private String mediaType;
    private String interactionType;
    private String createdAt;
    private String updatedAt;

    public Interaction(int id, int profileId, int mediaId, String mediaType, String interactionType, String createdAt, String updatedAt) {
        this.id = id;
        this.profileId = profileId;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.interactionType = interactionType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Interaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void sendInteraction(Context ctx) {
        // Get the profile from the shared preferences
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("profile", Context.MODE_PRIVATE);
        int profileId = sharedPreferences.getInt("id", 0);

        if (profileId == 0) {
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("mediaId", String.valueOf(this.getMediaId()));
        data.put("profileId", String.valueOf(profileId));
        data.put("mediaType", this.getMediaType());
        data.put("interactionType", this.getInteractionType());

        // Send the interaction to the server
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                try {
                    String result = APIClient.postMethodWithCookies("/profile/" + profileId + "/interaction/", data, ctx);
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
