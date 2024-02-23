package com.nassimlnd.flixhub.Model;

import java.sql.Date;

public class Media {
    private int id;
    private String title;
    private String tvg_id;
    private String tvg_name;
    private String tvg_logo;
    private String group_title;
    private String url;
    private Date createdAt;
    private Date updatedAt;

    public Media() {

    }

    public Media(int id, String title, String tvg_id, String tvg_name, String tvg_logo, String group_title, String url, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.tvg_id = tvg_id;
        this.tvg_name = tvg_name;
        this.tvg_logo = tvg_logo;
        this.group_title = group_title;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public String getTvg_id() {
        return tvg_id;
    }

    public void setTvg_id(String tvg_id) {
        this.tvg_id = tvg_id;
    }

    public String getTvg_name() {
        return tvg_name;
    }

    public void setTvg_name(String tvg_name) {
        this.tvg_name = tvg_name;
    }

    public String getTvg_logo() {
        return tvg_logo;
    }

    public void setTvg_logo(String tvg_logo) {
        this.tvg_logo = tvg_logo;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
