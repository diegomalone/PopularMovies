package com.diegomalone.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Diego Malone on 26/12/17.
 */

public class MovieReview implements Parcelable {

    private String author, reviewUrl, content, reviewId;

    public MovieReview(String author, String reviewUrl, String content, String reviewId) {
        this.author = author;
        this.reviewUrl = reviewUrl;
        this.content = content;
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.reviewUrl);
        dest.writeString(this.content);
        dest.writeString(this.reviewId);
    }

    protected MovieReview(Parcel in) {
        this.author = in.readString();
        this.reviewUrl = in.readString();
        this.content = in.readString();
        this.reviewId = in.readString();
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel source) {
            return new MovieReview(source);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
