package com.diegomalone.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by malone on 7/24/16.
 */

public class Movie implements Parcelable {

    private long id, voteCount;
    private String title, synopsis, releaseDate;
    private String poster, backgroundPhoto;
    private double userRating, popularity;

    public Movie(long id, String title, String synopsis, String releaseDate, String poster, String backgroundPhoto, double userRating) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.backgroundPhoto = backgroundPhoto;
        this.userRating = userRating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getFiveStarsRating() {
        return getUserRating() / 2;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.voteCount);
        dest.writeString(this.title);
        dest.writeString(this.synopsis);
        dest.writeString(this.releaseDate);
        dest.writeString(this.poster);
        dest.writeString(this.backgroundPhoto);
        dest.writeDouble(this.userRating);
        dest.writeDouble(this.popularity);
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.voteCount = in.readLong();
        this.title = in.readString();
        this.synopsis = in.readString();
        this.releaseDate = in.readString();
        this.poster = in.readString();
        this.backgroundPhoto = in.readString();
        this.userRating = in.readDouble();
        this.popularity = in.readDouble();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
