package com.diegomalone.popularmovies.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.adapter.GridAutofitLayoutManager;
import com.diegomalone.popularmovies.adapter.MovieGridAdapter;
import com.diegomalone.popularmovies.customview.EndlessRecyclerOnScrollListener;
import com.diegomalone.popularmovies.model.Movie;
import com.diegomalone.popularmovies.network.FetchMoviesTask;
import com.diegomalone.popularmovies.network.OnTaskCompleted;
import com.diegomalone.popularmovies.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;

import static com.diegomalone.popularmovies.fragment.SortDialogFragment.BROADCAST_SORT_PREFERENCES_CHANGED;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieShowcaseFragment extends Fragment implements OnTaskCompleted<List<Movie>>, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MovieShowcaseFragment.class.getSimpleName();

    private final String MOVIE_LIST = "movieList";
    private final String CURRENT_PAGE = "page";

    private RecyclerView mMovieRecyclerView;

    private SwipeRefreshLayout mSwapRefreshLayout;
    private MovieGridAdapter mMovieGridAdapter;
    private GridAutofitLayoutManager mLayoutManager;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    private int mCurrentPage = 1;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMovieList(1);
        }
    };

    public MovieShowcaseFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_movie_showcase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCurrentPage = 1;

        mMovieRecyclerView = view.findViewById(R.id.movie_list_recycler_view);
        mSwapRefreshLayout = view.findViewById(R.id.swipe_layout);

        mSwapRefreshLayout.setOnRefreshListener(this);

        // Size in px
        mLayoutManager = new GridAutofitLayoutManager(getActivity(), 500);
        mMovieGridAdapter = new MovieGridAdapter(getActivity());

        setupRecyclerView();

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST)) {
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            mMovieGridAdapter.addMovieList(movieList);
            mEndlessRecyclerOnScrollListener.setLoading(false);

            if (savedInstanceState.containsKey(CURRENT_PAGE)) {
                mCurrentPage = savedInstanceState.getInt(CURRENT_PAGE);
                mEndlessRecyclerOnScrollListener.setCurrentPage(mCurrentPage);
            }
        } else {
            updateMovieList(mCurrentPage);
        }
    }

    private void setupRecyclerView() {
        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setAdapter(mMovieGridAdapter);

        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                mSwapRefreshLayout.setRefreshing(true);
                updateMovieList(page);
            }
        };
        mMovieRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    private void updateMovieList(int page) {
        mSwapRefreshLayout.setRefreshing(true);
        mCurrentPage = page;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SortUtils sortUtils = new SortUtils(prefs);
        new FetchMoviesTask(this).execute(sortUtils.getSortPreference(), String.valueOf(page), getString(R.string.tmdb_api_key));
    }

    @Override
    public void onTaskCompleted(List<Movie> movieList) {
        mEndlessRecyclerOnScrollListener.setLoading(false);
        if (mCurrentPage == 1) {
            mMovieGridAdapter.setMovieList(movieList);
            mEndlessRecyclerOnScrollListener.reset();
        } else {
            mMovieGridAdapter.addMovieList(movieList);
        }

        mSwapRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_showcase, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        DialogFragment dialogFragment = new SortDialogFragment();
        dialogFragment.show(getFragmentManager(), SortDialogFragment.TAG);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.addAll(mMovieGridAdapter.getMovieList());
        outState.putInt(CURRENT_PAGE, mCurrentPage);
        outState.putParcelableArrayList(MOVIE_LIST, movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTaskError() {
        Toast.makeText(getActivity(), R.string.error_getting_movies, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_SORT_PREFERENCES_CHANGED));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        updateMovieList(1);
        mSwapRefreshLayout.setRefreshing(false);
    }
}
