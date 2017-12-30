package com.diegomalone.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKGROUND_PHOTO;
import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID;
import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER;
import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS;
import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE;
import static com.diegomalone.popularmovies.data.MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING;

/**
 * Created by Diego Malone on 29/12/17.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_SCHEMA_VERSION = 1;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovieContract.FavoriteMoviesEntry.TABLE_NAME + " (" +
                COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_MOVIE_TITLE + " TEXT, " +
                COLUMN_MOVIE_SYNOPSIS + " TEXT, " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                COLUMN_MOVIE_POSTER + " TEXT, " +
                COLUMN_MOVIE_BACKGROUND_PHOTO + " TEXT, " +
                COLUMN_MOVIE_USER_RATING + " REAL " +
                " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
