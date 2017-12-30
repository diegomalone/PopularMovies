package com.diegomalone.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.popularmovies.customview.MovieCardView;
import com.diegomalone.popularmovies.data.MovieContract;
import com.diegomalone.popularmovies.model.Movie;

/**
 * Created by Diego Malone on 30/12/17.
 */

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    public FavoriteMovieAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID) : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Nullable
    public Movie getItem(int position) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return null;
        }
        if (position < 0 || position > cursor.getCount()) {
            return null;
        }
        cursor.moveToFirst();
        for (int i = 0; i < position; i++) {
            cursor.moveToNext();
        }
        return Movie.fromCursor(cursor);
    }

    @Override
    public FavoriteMovieAdapter.FavoriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteMovieAdapter.FavoriteMovieViewHolder(new MovieCardView(mContext));
    }

    @Override
    public void onBindViewHolder(FavoriteMovieViewHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        onBindViewHolder(holder, mCursor);
    }

    private void onBindViewHolder(FavoriteMovieViewHolder viewHolder, Cursor cursor) {
        ((MovieCardView) viewHolder.itemView).setMovie(Movie.fromCursor(cursor));
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }

        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }

            mRowIdColumn = newCursor.getColumnIndexOrThrow(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID);
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
        }
    }

    public class FavoriteMovieViewHolder extends RecyclerView.ViewHolder {
        public FavoriteMovieViewHolder(View v) {
            super(v);
        }
    }
}
