package com.diegomalone.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.popularmovies.customview.MovieCardView;
import com.diegomalone.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malone on 7/24/16.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieGridViewHolder> {

    private List<Movie> mMovieList = new ArrayList<>();
    private Context mContext;

    public MovieGridAdapter(Context context) {
        this.mContext = context;
    }

    public void setMovieList(List<Movie> movieList) {
        mMovieList.clear();
        mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public MovieGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieGridViewHolder(new MovieCardView(mContext));
    }

    @Override
    public void onBindViewHolder(MovieGridViewHolder holder, int position) {
        ((MovieCardView) holder.itemView).setMovie(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MovieGridViewHolder extends RecyclerView.ViewHolder {
        public MovieGridViewHolder(View v) {
            super(v);
        }
    }
}
