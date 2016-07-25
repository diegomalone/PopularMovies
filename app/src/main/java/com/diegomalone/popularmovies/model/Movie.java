package com.diegomalone.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by malone on 7/24/16.
 */

public class Movie implements Parcelable {

    private String title, synopsis, releaseDate;
    private String poster, backgroundPhoto;
    private double userRating;

    public Movie(String title, String synopsis, String releaseDate, String poster, String backgroundPhoto, double userRating) {
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.backgroundPhoto = backgroundPhoto;
        this.userRating = userRating;
    }

    public Movie(Parcel parcel){
        String[] data = new String[6];
        parcel.readStringArray(data);

        this.title = data[0];
        this.synopsis = data[1];
        this.releaseDate = data[2];
        this.poster = data[3];
        this.backgroundPhoto = data[4];
        this.userRating = Double.valueOf(data[5]);
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public double getFiveStarsRating() {
        return getUserRating() / 2;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {getTitle(),
                getSynopsis(),
                getReleaseDate(),
                getPoster(),
                getBackgroundPhoto(),
                String.valueOf(getUserRating())});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
