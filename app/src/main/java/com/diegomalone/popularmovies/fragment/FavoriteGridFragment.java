package com.diegomalone.popularmovies.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.adapter.FavoriteMovieAdapter;
import com.diegomalone.popularmovies.adapter.GridAutofitLayoutManager;
import com.diegomalone.popularmovies.data.MovieContract;

/**
 * Created by Diego Malone on 30/12/17.
 */

public class FavoriteGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private RecyclerView mMovieRecyclerView;

    private SwipeRefreshLayout mSwapRefreshLayout;

    private FavoriteMovieAdapter mFavoriteMovieAdapter;
    private GridAutofitLayoutManager mLayoutManager;

    public FavoriteGridFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_showcase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMovieRecyclerView = view.findViewById(R.id.movie_list_recycler_view);
        mSwapRefreshLayout = view.findViewById(R.id.swipe_layout);

        mSwapRefreshLayout.setEnabled(false);

        // Size in px
        mLayoutManager = new GridAutofitLayoutManager(getActivity(), 500);
        mFavoriteMovieAdapter = new FavoriteMovieAdapter(getActivity(), null);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setAdapter(mFavoriteMovieAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavoriteMovieAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteMovieAdapter.changeCursor(null);
    }
}
