package com.diegomalone.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.data.FavoriteService;
import com.diegomalone.popularmovies.fragment.MovieDetailFragment;
import com.diegomalone.popularmovies.model.Movie;

/**
 * Created by malone on 7/25/16.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private FloatingActionButton mFavoriteFAB;
    private ViewGroup mCoordinatorLayout;

    private FavoriteService mFavoriteService;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFavoriteService = new FavoriteService(this);

        if (getIntent() != null && getIntent().hasExtra(Intent.EXTRA_INTENT)) {
            mMovie = getIntent().getParcelableExtra(Intent.EXTRA_INTENT);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new MovieDetailFragment())
                    .commit();
        }

        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mFavoriteFAB = findViewById(R.id.favorite_fab);
        mFavoriteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFavorite();
            }
        });
        updateFavoriteStatus();
    }

    private void clickFavorite() {
        if (mFavoriteService.isFavorite(mMovie)) {
            mFavoriteService.removeFromFavorites(mMovie);
            Snackbar.make(mCoordinatorLayout, R.string.favorites_removed, Snackbar.LENGTH_LONG).show();
        } else {
            mFavoriteService.addToFavorites(mMovie);
            Snackbar.make(mCoordinatorLayout, R.string.favorites_added, Snackbar.LENGTH_LONG).show();
        }
        updateFavoriteStatus();
    }

    private void updateFavoriteStatus() {
        if (mFavoriteService.isFavorite(mMovie)) {
            mFavoriteFAB.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mFavoriteFAB.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }
}
