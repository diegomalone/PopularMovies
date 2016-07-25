package com.diegomalone.popularmovies.customview;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.activity.MovieShowcaseActivity;
import com.diegomalone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by malone on 7/24/16.
 */

public class MovieCardView extends BaseCardView {

    private Movie mMovie;
    private ImageView mMoviePoster;

    public MovieCardView(Context context) {
        this(context, null);
    }

    public MovieCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.card_showcase_movie, this, true);

        init();
    }

    private void init() {
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster_image_view);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MovieShowcaseActivity) mContext).selectMovie(mMovie);
            }
        });
    }

    private void updateCardInfo() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(mContext.getString(R.string.movie_poster_base_url))
                .appendPath("t")
                .appendPath("p")
                .appendPath("w500")
                .appendPath(mMovie.getPoster());

        Uri posterUri = builder.build();

        Picasso.with(mContext)
                .load(posterUri)
                .placeholder(R.drawable.ic_movie_poster_placeholder)
                .error(R.drawable.ic_movie_poster_error)
                .fit()
                .into(mMoviePoster);
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
        updateCardInfo();
    }

}
