package com.diegomalone.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.popularmovies.customview.MovieReviewCardView;
import com.diegomalone.popularmovies.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Malone on 28/12/17.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private List<MovieReview> mMovieReviewList = new ArrayList<>();
    private Context context;

    public MovieReviewAdapter(Context context) {
        this.context = context;
    }

    public void setMovieList(List<MovieReview> movieReviewList) {
        mMovieReviewList.clear();
        mMovieReviewList.addAll(movieReviewList);
        notifyDataSetChanged();
    }

    public List<MovieReview> getMovieReviewList() {
        return mMovieReviewList;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieReviewViewHolder(new MovieReviewCardView(context));
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        ((MovieReviewCardView) holder.itemView).setMovieReview(mMovieReviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieReviewList.size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
        }
    }
}
