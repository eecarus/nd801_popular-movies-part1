package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.PosterViewHolder> {

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMoviePosterImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            this.mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_item_poster);
        }
    }

    public MovieListAdapter() {
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

}