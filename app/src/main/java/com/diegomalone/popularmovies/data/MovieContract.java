package com.diegomalone.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Diego Malone on 29/12/17.
 */

public class MovieContract {

    static final String CONTENT_AUTHORITY = "com.diegomalone.popularmovies";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_MOVIES = "movies";
    static final String PATH_FAVORITES = "favorites";

    private MovieContract() {
    }

    static final class FavoriteMoviesEntry implements BaseColumns {

        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES)
                .appendPath(PATH_FAVORITES)
                .build();

        static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY
                        + "/" + PATH_FAVORITES;

        static final String TABLE_NAME = "favorites";

        static final String COLUMN_MOVIE_ID = "movieId";
        static final String COLUMN_MOVIE_TITLE = "movieTitle";
        static final String COLUMN_MOVIE_SYNOPSIS = "movieSynopsis";
        static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        static final String COLUMN_MOVIE_POSTER = "moviePoster";
        static final String COLUMN_MOVIE_BACKGROUND_PHOTO = "movieBackgroundPhoto";
        static final String COLUMN_MOVIE_USER_RATING = "movieUserRating";

    }
}
