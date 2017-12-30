package com.diegomalone.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.fragment.FavoriteGridFragment;
import com.diegomalone.popularmovies.fragment.MovieShowcaseFragment;
import com.diegomalone.popularmovies.model.Movie;

public class MovieShowcaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String SELECTED_NAVIGATION_ITEM_KEY = "selectedMenu";

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private int mSelectedNavigationItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_showcase);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new MovieShowcaseFragment())
                    .commit();
        }

        mNavigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void selectMovie(Movie movie) {
        Intent intent = new Intent(MovieShowcaseActivity.this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, movie);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_NAVIGATION_ITEM_KEY, mSelectedNavigationItem);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectedNavigationItem = savedInstanceState.getInt(SELECTED_NAVIGATION_ITEM_KEY);
            Menu menu = mNavigationView.getMenu();
            menu.getItem(mSelectedNavigationItem).setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.menu_discover:
                if (mSelectedNavigationItem != 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new MovieShowcaseFragment())
                            .commit();
                    mSelectedNavigationItem = 0;
                }
                mDrawerLayout.closeDrawers();
                updateTitle();
                return true;
            case R.id.menu_favorites:
                if (mSelectedNavigationItem != 1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new FavoriteGridFragment())
                            .commit();
                    mSelectedNavigationItem = 1;
                }
                mDrawerLayout.closeDrawers();
                updateTitle();
                return true;
            default:
                return false;
        }
    }

    private void updateTitle() {
        if (mSelectedNavigationItem == 0) {
            setTitle(R.string.title_main_discover);
        } else {
            setTitle(R.string.title_main_favorites);
        }
    }
}