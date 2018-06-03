package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.moviedb.MovieDbService;
import com.example.android.popularmovies.moviedb.MovieSummary;
import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private static final String TAG = MovieListAdapter.class.getSimpleName();

    private final Context context;
    private MovieSummary[] movieSummaries;
    private MovieDbService movieService;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mMoviePosterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mMoviePosterImageView = itemView.findViewById(R.id.iv_movie_item_poster);
        }
    }

    public MovieListAdapter(Context context, MovieDbService movieService) {
        this.context = context;
        this.movieService = movieService;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the poster at this position
        MovieSummary summary = movieSummaries[position];
        holder.mMoviePosterImageView.setContentDescription(summary.getTitle());

        // convert to the full URL
        Uri uri = movieService.getFullImagePath(summary.getPosterPath());

        // use Picasso to load it
        Log.w(TAG, "loading " + uri + " with Picasso");
        Picasso.with(context)
                .load(uri.toString())
                .fit()
//                .centerCrop()
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
                .into(holder.mMoviePosterImageView);

    }

    @Override
    public int getItemCount() {
        return movieSummaries == null ? 0 : movieSummaries.length;
    }

    public void setMovieSummaries(MovieSummary[] movieSummaries) {
        this.movieSummaries = movieSummaries;
        notifyDataSetChanged();   // from Lesson on RecyclerViews
    }

}