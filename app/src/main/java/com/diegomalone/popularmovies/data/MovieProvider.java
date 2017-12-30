package com.diegomalone.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Diego Malone on 29/12/17.
 */

public class MovieProvider extends ContentProvider {

    static final int FAVORITES = 101;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private MovieDatabaseHelper movieDatabaseHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIES + "/" +
                MovieContract.PATH_FAVORITES, FAVORITES);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDatabaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case FAVORITES:
                return MovieContract.FavoriteMoviesEntry.CONTENT_DIR_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int match = URI_MATCHER.match(uri);
        Cursor cursor;

        switch (match) {
            case FAVORITES:
                cursor = movieDatabaseHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                return null;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        if (match == FAVORITES) {
            long id = db.insert(MovieContract.FavoriteMoviesEntry.TABLE_NAME, null, contentValues);

            if (id <= 0) throw new SQLException("Failed to insert row: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return MovieContract.FavoriteMoviesEntry.CONTENT_URI;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsDeleted;

        switch (match) {
            case FAVORITES:
                rowsDeleted = db.delete(MovieContract.FavoriteMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public void shutdown() {
        movieDatabaseHelper.close();
        super.shutdown();
    }
}
