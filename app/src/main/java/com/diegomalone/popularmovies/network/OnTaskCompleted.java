package com.diegomalone.popularmovies.network;

/**
 * Created by malone on 7/24/16.
 */

public interface OnTaskCompleted<T> {
    void onTaskCompleted(T param);

    void onTaskError();
}
