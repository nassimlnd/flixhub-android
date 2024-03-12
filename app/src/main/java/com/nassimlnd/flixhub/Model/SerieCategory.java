package com.nassimlnd.flixhub.Model;

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
