package com.diegomalone.popularmovies.customview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.model.MovieVideo;
import com.squareup.picasso.Picasso;

/**
 * Created by Diego Malone on 28/12/17.
 */

public class MovieVideoCardView extends LinearLayout {

    private MovieVideo movieVideo;
    private ImageView movieVideoThumbnail;

    private Context context;

    public MovieVideoCardView(Context context) {
        this(context, null);
    }

    public MovieVideoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.card_movie_video, this, true);

        init();
    }

    private void init() {
        movieVideoThumbnail = findViewById(R.id.movie_video_thumbnail);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieVideo != null && movieVideo.isYouTubeVideo()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(context.getString(R.string.details_video_youtube_url_pattern, movieVideo.getKey())));
                    context.startActivity(intent);
                }
            }
        });
    }

    private void updateCardInfo() {
        Picasso.with(context)
                .load(context.getString(R.string.details_video_youtube_pattern, movieVideo.getKey()))
                .placeholder(R.drawable.bg_movie_video_placeholder)
                .error(R.drawable.bg_movie_video_placeholder)
                .fit()
                .into(movieVideoThumbnail);
    }

    public void setMovieVideo(MovieVideo movieVideo) {
        this.movieVideo = movieVideo;
        updateCardInfo();
    }

}
