package com.diegomalone.popularmovies.model;

/**
 * Created by malone on 7/24/16.
 */

public class Movie {

    private String title;
    private String poster, backgroundPhoto;

    public Movie(String title, String poster, String backgroundPhoto) {
        this.title = title;
        this.poster = poster;
        this.backgroundPhoto = backgroundPhoto;
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

    public String getBackgroundPhoto() {
        return backgroundPhoto;
    }

    public void setBackgroundPhoto(String backgroundPhoto) {
        this.backgroundPhoto = backgroundPhoto;
    }
}
