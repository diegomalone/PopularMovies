package com.diegomalone.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.model.Movie;
import com.diegomalone.popularmovies.model.MovieReview;
import com.diegomalone.popularmovies.model.MovieVideo;
import com.diegomalone.popularmovies.network.FetchMovieReviewsTask;
import com.diegomalone.popularmovies.network.FetchMovieVideosTask;
import com.diegomalone.popularmovies.network.OnTaskCompleted;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * Created by malone on 7/25/16.
 */

public class MovieDetailFragment extends Fragment {

    private TextView mMovieTitleTextView, mMovieSynopsisTextView, mMovieReleaseDateTextView, mMovieRatingTextView;
    private ImageView mMovieThumbnailImageView;
    private RatingBar mMovieRatingBar;
    private Movie mMovie;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMovieTitleTextView = view.findViewById(R.id.movie_title_text_view);
        mMovieSynopsisTextView = view.findViewById(R.id.movie_synopsis_text_view);
        mMovieReleaseDateTextView = view.findViewById(R.id.movie_release_date_text_view);
        mMovieThumbnailImageView = view.findViewById(R.id.movie_thumbnail_image_view);
        mMovieRatingBar = view.findViewById(R.id.movie_rating_rating_bar);
        mMovieRatingTextView = view.findViewById(R.id.movie_rating_text_view);

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

    @Override
    public void onStart() {
        super.onStart();

        loadMovieVideos();
        loadMovieReviews();
    }

    private void loadMovieVideos() {
        new FetchMovieVideosTask(movieVideoListOnTaskCompleted).execute(String.valueOf(mMovie.getId()), getString(R.string.tmdb_api_key));
    }

    private void loadMovieReviews() {
        new FetchMovieReviewsTask(movieReviewListOnTaskCompleted).execute(String.valueOf(mMovie.getId()), getString(R.string.tmdb_api_key));
    }

    OnTaskCompleted<List<MovieVideo>> movieVideoListOnTaskCompleted = new OnTaskCompleted<List<MovieVideo>>() {
        @Override
        public void onTaskCompleted(List<MovieVideo> movieVideos) {
            Log.i(this.getClass().getSimpleName(), "onTaskCompleted: " + movieVideos);
        }

        @Override
        public void onTaskError() {
            Toast.makeText(getActivity(), R.string.error_getting_movie_videos, Toast.LENGTH_LONG).show();
        }
    };

    OnTaskCompleted<List<MovieReview>> movieReviewListOnTaskCompleted = new OnTaskCompleted<List<MovieReview>>() {
        @Override
        public void onTaskCompleted(List<MovieReview> movieReviews) {
            Log.i(this.getClass().getSimpleName(), "onTaskCompleted: " + movieReviews);
        }

        @Override
        public void onTaskError() {
            Toast.makeText(getActivity(), R.string.error_getting_movie_videos, Toast.LENGTH_LONG).show();
        }
    };
}
