package com.diegomalone.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.popularmovies.customview.MovieVideoCardView;
import com.diegomalone.popularmovies.model.MovieVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Malone on 28/12/17.
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoViewHolder> {

    private List<MovieVideo> mMovieVideoList = new ArrayList<>();
    private Context context;

    public MovieVideoAdapter(Context context) {
        this.context = context;
    }

    public void setMovieList(List<MovieVideo> movieVideoList) {
        mMovieVideoList.clear();
        for (MovieVideo movieVideo : movieVideoList) {
            if (movieVideo.isYouTubeVideo()) {
                mMovieVideoList.add(movieVideo);
            }
        }
        notifyDataSetChanged();
    }

    public List<MovieVideo> getMovieVideoList() {
        return mMovieVideoList;
    }

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieVideoViewHolder(new MovieVideoCardView(context));
    }

    @Override
    public void onBindViewHolder(MovieVideoViewHolder holder, int position) {
        ((MovieVideoCardView) holder.itemView).setMovieVideo(mMovieVideoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieVideoList.size();
    }

    public class MovieVideoViewHolder extends RecyclerView.ViewHolder {

        public MovieVideoViewHolder(View itemView) {
            super(itemView);
        }
    }
}
