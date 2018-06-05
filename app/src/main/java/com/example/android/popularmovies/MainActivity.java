package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
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
        MovieListAdapter.MovieAdapterClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIEDB_LOADER = 37;
    private static final String LAYOUT_MANAGER_STATE_KEY = "layoutManager.state";

    private RecyclerView mMoviePostersRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgressIndicator;
    private MovieListAdapter mMovieListAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // locate views
        mMoviePostersRecyclerView = findViewById(R.id.rv_movie_posters_list);
        mErrorMessageTextView = findViewById(R.id.tv_error_message_display);
        mProgressIndicator = findViewById(R.id.pb_loading_indicator);

        // create the GridLayoutManager for the recycler view, set span to a calculated value
        mGridLayoutManager = new GridLayoutManager(MainActivity.this, calculateSpan());
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridLayoutManager.setReverseLayout(false);

        // initialize the service
        MovieDbService.initializeIfNeeded(getApplicationContext());

        // create the Adapter
        mMovieListAdapter = new MovieListAdapter(MainActivity.this, this);

        // attach the Recycler view and set its attributes
        mMoviePostersRecyclerView.setLayoutManager(mGridLayoutManager);
        mMoviePostersRecyclerView.setHasFixedSize(true);
        mMoviePostersRecyclerView.setAdapter(mMovieListAdapter);

        // register listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

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
        switch (selectedId) {
            case R.id.action_refresh:
                loadMovieData();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------------------------------
    // The section on Android Lifecycle (Loaders) comes from Lesson 2:7.17
    //
    // The following methods implement LoaderManager.LoaderCallbacks()
    // The framework automatically calls the onStartLoading() when the Activity resumes, so caching
    // is needed to prevent an unlimited number of network calls.
    // ---------------------------------------------------------------------------------------------

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<MovieSummaryResults> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieSummaryResults>(MainActivity.this) {

            MovieSummaryResults results;

            @Override
            public void deliverResult(@Nullable MovieSummaryResults data) {
                results = data;
                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if (results != null) {
                    deliverResult(results);
                } else {
                    forceLoad();
                    showLoadingViews();
                }
            }

            @Override
            public MovieSummaryResults loadInBackground() {
                // SystemClock.sleep(1000);

                // check for internet connectivity
                if (ApiUtils.isNetworkReadyForUse(getContext(), true)) {
                    String type = getMovieListType();
                    // for now, hardcode as most popular
                    try {
                        return MovieDbService.getMovieSummaryResults(type);
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to retrieve movie summaries", e);
                    }
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieSummaryResults> loader, MovieSummaryResults data) {
        if (data != null) {
            // save the data
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

//    private void loadFakeMovieData() {
//        mMovieListAdapter.setMovieSummaries(MovieDbService.getFakeData(MainActivity.this).getResults());
//    }

    private String getMovieListType() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(getString(R.string.pref_sortedby_key),
                getString(R.string.pref_sortedby_key_popular));
    }

    // ---------------------------------------------------------------------------------------------
    // Activity Lifecycle transitions
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mGridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY))
            mGridLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    // ---------------------------------------------------------------------------------------------
    // Implementations for SharedPreferences.OnSharedPreferenceChangeListener
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.pref_sortedby_key).equals(key)) {
            loadMovieData();    // force a reload, later we will add caching on the Loader to avoid repetitive hits
        }
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
        String spanCount = getResources().getString(R.string.recycler_view_span_count);
        return Integer.parseInt(spanCount);
    }
}