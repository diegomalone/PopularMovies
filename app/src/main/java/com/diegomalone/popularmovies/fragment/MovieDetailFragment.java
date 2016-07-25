package com.diegomalone.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by malone on 7/25/16.
 */

public class MovieDetailFragment extends Fragment {

    private TextView mMovieTitleTextView, mMovieSynopsisTextView, mMovieReleaseDateTextView;
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

        mMovieTitleTextView = (TextView) view.findViewById(R.id.movie_title_text_view);
        mMovieSynopsisTextView = (TextView) view.findViewById(R.id.movie_synopsis_text_view);
        mMovieReleaseDateTextView = (TextView) view.findViewById(R.id.movie_release_date_text_view);
        mMovieThumbnailImageView = (ImageView) view.findViewById(R.id.movie_thumbnail_image_view);
        mMovieRatingBar = (RatingBar) view.findViewById(R.id.movie_rating_rating_bar);

        mMovieTitleTextView.setText(mMovie.getTitle());
        mMovieSynopsisTextView.setText(mMovie.getSynopsis());
        mMovieReleaseDateTextView.setText(mMovie.getReleaseDate());
        mMovieRatingBar.setRating((float) mMovie.getFiveStarsRating());


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
}
