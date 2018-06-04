package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.moviedb.ApiUtils;
import com.example.android.popularmovies.moviedb.MovieDbService;
import com.example.android.popularmovies.moviedb.MovieSummary;
import com.example.android.popularmovies.moviedb.MovieSummaryResults;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieSummaryResults>,
        MovieListAdapter.MovieAdapterClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIEDB_LOADER = 37;
    private static final String MOVIEDB_SERVICE_KEY = "moviedb.service";
    private static MovieDbService movieService;

    private RecyclerView mMoviePostersRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgressIndicator;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // locate views
        mMoviePostersRecyclerView = findViewById(R.id.rv_movie_posters_list);
        mErrorMessageTextView = findViewById(R.id.tv_error_message_display);
        mProgressIndicator = findViewById(R.id.pb_loading_indicator);

        // create the GridLayoutManager for the recycler view, set span to a calculated value
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, calculateSpan());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);

        // initialize the service
        MovieDbService.initializeIfNeeded(getApplicationContext());

        // create the Adapter
        mMovieListAdapter = new MovieListAdapter(MainActivity.this, this);

        // attach the Recycler view and set its attributes
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);
        mMoviePostersRecyclerView.setHasFixedSize(true);
        mMoviePostersRecyclerView.setAdapter(mMovieListAdapter);

        // initialize the loader
        loadMovieData();
    }

    // ---------------------------------------------------------------------------------------------
    // The section on Menus comes from Lesson 2:2
    //
    // The following methods initialize the menu and respond to the initial 'Refresh' action,
    // although nothing is actually done yet.
    // ---------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        if (selectedId == R.id.action_refresh) {
            loadMovieData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------------------------------
    // The section on Android Lifecycle (Loaders) comes from Lesson 2:7.17
    //
    // The following methods implement LoaderManager.LoaderCallbacks()
    // ---------------------------------------------------------------------------------------------

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<MovieSummaryResults> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieSummaryResults>(MainActivity.this) {
            @Override
            protected void onStartLoading() {
                // there are really no arguments, because we will get the data from Preferences
                forceLoad();
            }

            @Override
            public MovieSummaryResults loadInBackground() {
                String type = getContext().getString(R.string.pref_sortedby_key_popular);
                // for now, hardcode as most popular
                try {
                    return MovieDbService.getMovieSummaryResults(getContext(),type);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to retrieve movie summaries", e);
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieSummaryResults> loader, MovieSummaryResults data) {
        if (data != null) {
            // save the data
            Log.w(TAG, "got here wih data!");
            showDataView();
            mMovieListAdapter.setMovieSummaries(data.getResults());
        } else {
            showErrorMessageView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieSummaryResults> loader) {

    }

    // ---------------------------------------------------------------------------------------------
    // Implementation of MovieListAdapter.MovieAdapterClickHandler
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onClick(MovieSummary summary, ImageView imageView) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.MOVIE_SUMMARY_KEY, summary);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        imageView,
                        ViewCompat.getTransitionName(imageView));
        startActivity(detailIntent, options.toBundle());
    }

    // ---------------------------------------------------------------------------------------------
    // The following methods deal with changing the data on the adapter.
    // ---------------------------------------------------------------------------------------------

    private void loadMovieData() {
        Bundle args = new Bundle();
        // args.putString(SEARCH_QUERY_URL_EXTRA, githubSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieSummaryResults> loader = loaderManager.getLoader(MOVIEDB_LOADER);
        if (loader == null)
            loaderManager.initLoader(MOVIEDB_LOADER, args, this);
        else
            loaderManager.restartLoader(MOVIEDB_LOADER, args, this);
    }

    private void loadFakeMovieData() {
        mMovieListAdapter.setMovieSummaries(MovieDbService.getFakeData(MainActivity.this).getResults());
    }

    // ---------------------------------------------------------------------------------------------
    // Data loading transitions
    // ---------------------------------------------------------------------------------------------

    private void showLoadingViews() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mMoviePostersRecyclerView.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mMoviePostersRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessageView() {
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mMoviePostersRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    // ---------------------------------------------------------------------------------------------
    // Various utility methods
    // ---------------------------------------------------------------------------------------------

    private int calculateSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 5;
        else
            return 3;
    }
}