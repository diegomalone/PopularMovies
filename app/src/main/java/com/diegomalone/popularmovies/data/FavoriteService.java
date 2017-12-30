package com.diegomalone.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.diegomalone.popularmovies.model.Movie;

/**
 * Created by Diego Malone on 29/12/17.
 */

public class FavoriteService {

    private final Context mContext;

    public FavoriteService(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isFavorite(Movie movie) {
        boolean favorite = false;

        Cursor cursor = mContext.getContentResolver().query(MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = " + movie.getId(),
                null,
                null);

        if (cursor != null) {
            favorite = cursor.getCount() > 0;
            cursor.close();
        }

        return favorite;
    }

    public void addToFavorites(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS, movie.getSynopsis());
        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER, movie.getPoster());
        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKGROUND_PHOTO, movie.getBackgroundPhoto());
        contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING, movie.getUserRating());

        mContext.getContentResolver().insert(MovieContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);
    }

    public void removeFromFavorites(Movie movie) {
        mContext.getContentResolver().delete(
                MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = " + movie.getId(),
                null
        );
    }
}
