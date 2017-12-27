package com.diegomalone.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.fragment.MovieShowcaseFragment;
import com.diegomalone.popularmovies.fragment.SortDialogFragment;
import com.diegomalone.popularmovies.model.Movie;

public class MovieShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_showcase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new MovieShowcaseFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_showcase, menu);
        return true;
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

    public void selectMovie(Movie movie) {
        Intent intent = new Intent(MovieShowcaseActivity.this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, movie);
        startActivity(intent);
    }

    private void showSortDialog() {
        DialogFragment dialogFragment = new SortDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), SortDialogFragment.TAG);
    }
}