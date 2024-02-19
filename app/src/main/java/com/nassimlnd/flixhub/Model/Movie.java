package com.nassimlnd.flixhub.Model;

import java.sql.Date;

public class Movie extends Media {

    public Movie() {
        super();
    }

    public Movie(int id, String title, String tvg_id, String tvg_name, String tvg_logo, String group_title, String url, Date createdAt, Date updatedAt) {
        super(id, title, tvg_id, tvg_name, tvg_logo, group_title, url, createdAt, updatedAt);
    }


}
