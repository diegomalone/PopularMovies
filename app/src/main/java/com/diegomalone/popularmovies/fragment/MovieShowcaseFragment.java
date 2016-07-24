package com.diegomalone.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.adapter.GridAutoFitLayoutManager;
import com.diegomalone.popularmovies.adapter.MovieGridAdapter;
import com.diegomalone.popularmovies.model.Movie;
import com.diegomalone.popularmovies.network.FetchMoviesTask;
import com.diegomalone.popularmovies.network.OnTaskCompleted;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieShowcaseFragment extends Fragment implements OnTaskCompleted<List<Movie>> {

    private RecyclerView mMovieRecyclerView;

    private MovieGridAdapter mMovieGridAdapter;
    private GridAutoFitLayoutManager mLayoutManager;

    public MovieShowcaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_showcase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMovieRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list_recycler_view);

        // TODO Get image size programmatically
        // Size in px
        mLayoutManager = new GridAutoFitLayoutManager(getActivity(), 500);
        mMovieGridAdapter = new MovieGridAdapter(getActivity());
        mMovieRecyclerView.setLayoutManager(mLayoutManager);

        // TODO Get type from preferences
        new FetchMoviesTask(this).execute("popular", getString(R.string.tmdb_api_key));
    }

    @Override
    public void onTaskCompleted(List<Movie> movieList) {
        mMovieGridAdapter.setMovieList(movieList);
        mMovieRecyclerView.setAdapter(mMovieGridAdapter);
    }

    @Override
    public void onTaskError() {
        Toast.makeText(getActivity(), "Error getting movies", Toast.LENGTH_LONG).show();
    }
}
