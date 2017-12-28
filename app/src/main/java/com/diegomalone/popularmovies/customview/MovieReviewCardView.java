package com.diegomalone.popularmovies.customview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.model.MovieReview;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Diego Malone on 28/12/17.
 */

public class MovieReviewCardView extends LinearLayout {

    private MovieReview movieReview;
    private TextView authorTextView, contentTextView;

    private Context context;

    public MovieReviewCardView(Context context) {
        this(context, null);
    }

    public MovieReviewCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.card_movie_review, this, true);

        init();
    }

    private void init() {
        authorTextView = findViewById(R.id.review_author_text_view);
        contentTextView = findViewById(R.id.review_content_text_view);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieReview != null && StringUtils.isNotBlank(movieReview.getReviewUrl())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReview.getReviewUrl()));
                    context.startActivity(intent);
                }
            }
        });
    }

    private void updateCardInfo() {
        authorTextView.setText(movieReview.getAuthor());
        contentTextView.setText(movieReview.getContent());
    }

    public void setMovieReview(MovieReview movieReview) {
        this.movieReview = movieReview;
        updateCardInfo();
    }

}
