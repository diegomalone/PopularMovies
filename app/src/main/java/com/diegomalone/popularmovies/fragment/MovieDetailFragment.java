package com.diegomalone.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.adapter.MovieReviewAdapter;
import com.diegomalone.popularmovies.adapter.MovieVideoAdapter;
import com.diegomalone.popularmovies.customview.CustomDividerItemDecoration;
import com.diegomalone.popularmovies.model.Movie;
import com.diegomalone.popularmovies.model.MovieReview;
import com.diegomalone.popularmovies.model.MovieVideo;
import com.diegomalone.popularmovies.network.FetchMovieReviewsTask;
import com.diegomalone.popularmovies.network.FetchMovieVideosTask;
import com.diegomalone.popularmovies.network.OnTaskCompleted;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by malone on 7/25/16.
 */

public class MovieDetailFragment extends Fragment {

    public static final String TAG = MovieDetailFragment.class.getSimpleName();

    public static final String SCROLL_POSITION = "scrollPosition";
    public static final String MOVIE_REVIEW_LIST = "movieReviewList";
    public static final String MOVIE_VIDEO_LIST = "movieVideoList";

    private TextView mMovieTitleTextView;
    private TextView mMovieSynopsisTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieRatingTextView;
    private TextView mNoMovieReviewsTextView;
    private TextView mNoMovieVideosTextView;
    private TextView mMovieReviewsLoadingTextView;
    private TextView mMovieVideosLoadingTextView;
    private ImageView mMovieThumbnailImageView;
    private RatingBar mMovieRatingBar;
    private NestedScrollView mScrollView;

    private Movie mMovie;

    private RecyclerView mVideoRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private MovieReviewAdapter mMovieReviewAdapter;
    private MovieVideoAdapter mMovieVideoAdapter;
    private LinearLayoutManager mMovieReviewLayoutManager;
    private LinearLayoutManager mMovieVideoLayoutManager;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_INTENT)) {
            mMovie = intent.getParcelableExtra(Intent.EXTRA_INTENT);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerViews();

        showMovieDetails();

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_REVIEW_LIST)) {
            ArrayList<MovieReview> movieReviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEW_LIST);
            showMovieReviewList(movieReviews);
        } else {
            loadMovieReviews();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_VIDEO_LIST)) {
            ArrayList<MovieVideo> movieVideos = savedInstanceState.getParcelableArrayList(MOVIE_VIDEO_LIST);
            showMovieVideoList(movieVideos);
        } else {
            loadMovieVideos();
        }

        mScrollView.post(new Runnable() {
            public void run() {
                int scrollPosition = 0;
                if (savedInstanceState != null && savedInstanceState.containsKey(SCROLL_POSITION)) {
                    scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0);
                }

                mScrollView.scrollTo(0, scrollPosition);
            }
        });
    }

    private void showMovieDetails() {
        mMovieTitleTextView.setText(mMovie.getTitle());
        mMovieSynopsisTextView.setText(mMovie.getSynopsis());
        mMovieReleaseDateTextView.setText(getString(R.string.release_date_placeholder, mMovie.getReleaseDate()));
        mMovieRatingBar.setRating((float) mMovie.getFiveStarsRating());
        mMovieRatingTextView.setText(String.format(Locale.ENGLISH, "%.1f", mMovie.getFiveStarsRating()));

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(getActivity().getString(R.string.movie_poster_base_url))
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(mMovie.getPoster());

        Uri posterUri = builder.build();

        Picasso.with(getActivity())
                .load(posterUri)
                .placeholder(R.drawable.ic_movie_poster_placeholder)
                .error(R.drawable.ic_movie_poster_error)
                .fit()
                .into(mMovieThumbnailImageView);
    }

    private void initViews(@NonNull View view) {
        mMovieTitleTextView = view.findViewById(R.id.movie_title_text_view);
        mMovieSynopsisTextView = view.findViewById(R.id.movie_synopsis_text_view);
        mMovieReleaseDateTextView = view.findViewById(R.id.movie_release_date_text_view);
        mMovieThumbnailImageView = view.findViewById(R.id.movie_thumbnail_image_view);
        mMovieRatingBar = view.findViewById(R.id.movie_rating_rating_bar);
        mMovieRatingTextView = view.findViewById(R.id.movie_rating_text_view);
        mNoMovieReviewsTextView = view.findViewById(R.id.review_list_empty_text_view);
        mNoMovieVideosTextView = view.findViewById(R.id.video_list_empty_text_view);
        mMovieReviewsLoadingTextView = view.findViewById(R.id.review_list_loading_text_view);
        mMovieVideosLoadingTextView = view.findViewById(R.id.video_list_loading_text_view);
        mScrollView = view.findViewById(R.id.scroll_view);

        mVideoRecyclerView = view.findViewById(R.id.video_list_recycler_view);
        mReviewsRecyclerView = view.findViewById(R.id.review_list_recycler_view);
    }

    private void setupRecyclerViews() {
        mMovieReviewAdapter = new MovieReviewAdapter(getContext());
        mMovieReviewLayoutManager = new LinearLayoutManager(getContext());
        mReviewsRecyclerView.setLayoutManager(mMovieReviewLayoutManager);
        mReviewsRecyclerView.setAdapter(mMovieReviewAdapter);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);

        mReviewsRecyclerView.addItemDecoration(new CustomDividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider)));

        mMovieVideoAdapter = new MovieVideoAdapter(getContext());
        mMovieVideoLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        mVideoRecyclerView.setLayoutManager(mMovieVideoLayoutManager);
        mVideoRecyclerView.setAdapter(mMovieVideoAdapter);
    }

    private void loadMovieVideos() {
        new FetchMovieVideosTask(movieVideoListOnTaskCompleted).execute(String.valueOf(mMovie.getId()), getString(R.string.tmdb_api_key));
    }

    private void loadMovieReviews() {
        new FetchMovieReviewsTask(movieReviewListOnTaskCompleted).execute(String.valueOf(mMovie.getId()), getString(R.string.tmdb_api_key));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        ArrayList<MovieReview> movieReviews = new ArrayList<>();
        movieReviews.addAll(mMovieReviewAdapter.getMovieReviewList());
        ArrayList<MovieVideo> movieVideos = new ArrayList<>();
        movieVideos.addAll(mMovieVideoAdapter.getMovieVideoList());

        outState.putInt(SCROLL_POSITION, mScrollView.getScrollY());
        outState.putParcelableArrayList(MOVIE_REVIEW_LIST, movieReviews);
        outState.putParcelableArrayList(MOVIE_VIDEO_LIST, movieVideos);
        super.onSaveInstanceState(outState);
    }

    OnTaskCompleted<List<MovieVideo>> movieVideoListOnTaskCompleted = new OnTaskCompleted<List<MovieVideo>>() {
        @Override
        public void onTaskCompleted(List<MovieVideo> movieVideos) {
            showMovieVideoList(movieVideos);
        }

        @Override
        public void onTaskError() {
            Toast.makeText(getActivity(), R.string.error_getting_movie_videos, Toast.LENGTH_LONG).show();
        }
    };

    OnTaskCompleted<List<MovieReview>> movieReviewListOnTaskCompleted = new OnTaskCompleted<List<MovieReview>>() {
        @Override
        public void onTaskCompleted(List<MovieReview> movieReviews) {
            showMovieReviewList(movieReviews);
        }

        @Override
        public void onTaskError() {
            Toast.makeText(getActivity(), R.string.error_getting_movie_reviews, Toast.LENGTH_LONG).show();
        }
    };

    private void showMovieVideoList(List<MovieVideo> movieVideos) {
        mMovieVideosLoadingTextView.setVisibility(GONE);
        if (movieVideos.size() > 0) {
            mMovieVideoAdapter.setMovieList(movieVideos);
            mVideoRecyclerView.setVisibility(VISIBLE);
            mNoMovieVideosTextView.setVisibility(GONE);
        } else {
            mVideoRecyclerView.setVisibility(GONE);
            mNoMovieVideosTextView.setVisibility(VISIBLE);
        }
    }

    private void showMovieReviewList(List<MovieReview> movieReviews) {
        mMovieReviewsLoadingTextView.setVisibility(GONE);
        if (movieReviews.size() > 0) {
            mMovieReviewAdapter.setMovieList(movieReviews);
            mReviewsRecyclerView.setVisibility(VISIBLE);
            mNoMovieReviewsTextView.setVisibility(GONE);
        } else {
            mReviewsRecyclerView.setVisibility(GONE);
            mNoMovieReviewsTextView.setVisibility(VISIBLE);
        }
    }
}
