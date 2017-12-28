package com.diegomalone.popularmovies.customview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Diego Malone on 28/12/17.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int totalItemCount, lastVisibleItem;

    private int currentPage = 1;

    private GridLayoutManager gridLayoutManager;

    public EndlessRecyclerOnScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = gridLayoutManager.getItemCount();
        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

        boolean endHasBeenReached = lastVisibleItem + visibleThreshold >= totalItemCount;
        if (!loading && totalItemCount > 0 && endHasBeenReached) {
            // Do something
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void reset() {
        currentPage = 1;
    }

    public abstract void onLoadMore(int currentPage);
}

