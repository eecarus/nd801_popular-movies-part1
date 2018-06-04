package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
    private MovieAdapterClickHandler onClickHandler;

    public MovieListAdapter(Context context, MovieAdapterClickHandler onClickHandler) {
        this.context = context;
        this.onClickHandler = onClickHandler;
    }

    // ---------------------------------------------------------------------------------------------
    // Methods needed for RecyclerView.Adapter
    // ---------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the poster at this position
        MovieSummary summary = movieSummaries[position];
        holder.mMoviePosterImageView.setContentDescription(summary.getTitle());

        // convert to the full URL
        Uri uri = MovieDbService.getFullImagePath(summary.getPosterPath());

        // use Picasso to load it
        Picasso.with(context)
                .load(uri.toString())
                .fit()
//              .centerCrop()
                .placeholder(R.drawable.ic_movie)
                .error(R.drawable.ic_movie)
                .into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return movieSummaries == null ? 0 : movieSummaries.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mMoviePosterImageView;

        private ViewHolder(View itemView) {
            super(itemView);
            this.mMoviePosterImageView = itemView.findViewById(R.id.iv_movie_item_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            onClickHandler.onClick(movieSummaries[adapterPosition], this.mMoviePosterImageView);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Methods needed for an OnClick Listener
    // ---------------------------------------------------------------------------------------------

    public interface MovieAdapterClickHandler {
        void onClick(MovieSummary summary, ImageView imageView);
    }


    // ---------------------------------------------------------------------------------------------
    // Methods needed to update the adapter when data is loaded
    // ---------------------------------------------------------------------------------------------

    public void setMovieSummaries(MovieSummary[] movieSummaries) {
        this.movieSummaries = movieSummaries;
        notifyDataSetChanged();   // from Lesson on RecyclerViews
    }

}