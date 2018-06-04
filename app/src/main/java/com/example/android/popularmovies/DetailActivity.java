package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.moviedb.MovieDbService;
import com.example.android.popularmovies.moviedb.MovieSummary;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_SUMMARY_KEY = "movie.summary";

    private TextView mOriginalTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;
    private TextView mOverviewTextView;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        // locate view objects
        mOriginalTitleTextView = findViewById(R.id.tv_detail_original_title);
        mReleaseDateTextView = findViewById(R.id.tv_detail_release_date);
        mUserRatingTextView = findViewById(R.id.tv_detail_user_rating);
        mOverviewTextView = findViewById(R.id.tv_detail_overview);
        mPosterImageView = findViewById(R.id.iv_detail_poster);

        // load the data from the Intent
        Intent myIntent = getIntent();
        if (myIntent != null) {
            if (myIntent.hasExtra(MOVIE_SUMMARY_KEY)) {
                MovieSummary movieSummary = myIntent.getParcelableExtra(MOVIE_SUMMARY_KEY);
                populateView(movieSummary);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Implement Menu and Option handling
    // ---------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------------------------------
    // Other implementation methods
    // ---------------------------------------------------------------------------------------------

    /**
     * Populate the view using the supplied movie summary object
     *
     * @param movieSummary the summary data for the movie that the user clicked on.
     */
    private void populateView(MovieSummary movieSummary) {
        // set the title
        mOriginalTitleTextView.setText(movieSummary.getOriginalTitle());

        // set the release date
        String releaseDate = movieSummary.getReleaseDate().toString("MM/dd/yyyy");
        mReleaseDateTextView.setText(releaseDate);

        // set the user rating
        mUserRatingTextView.setText(movieSummary.getVoteAverage());

        // set the overview
        mOverviewTextView.setText(movieSummary.getOverview());

        // set the image
        mPosterImageView.setContentDescription("Poster for " + movieSummary.getOriginalTitle());
        Uri uri = MovieDbService.getFullImagePath(movieSummary.getPosterPath());
        Picasso.with(DetailActivity.this)
                .load(uri.toString())
                .fit()
//              .centerCrop()
                .placeholder(R.drawable.ic_movie)
                .error(R.drawable.ic_movie)
                .into(mPosterImageView);
    }
}
