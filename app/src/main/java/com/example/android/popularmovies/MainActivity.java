package com.example.android.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies.moviedb.ApiUtils;
import com.example.android.popularmovies.moviedb.MovieDbService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mMoviePostersRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create the GridLayoutManager for the recycler view, set span to a calculated value
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, calculateSpan());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);

        // create the service
        MovieDbService movieService = new MovieDbService(MainActivity.this);

        // create the Adapter
        mMovieListAdapter = new MovieListAdapter(MainActivity.this, movieService);
        mMovieListAdapter.setMovieSummaries(ApiUtils.getFakeData(MainActivity.this).getResults());

        // attach the Recycler view and set its attributes
        mMoviePostersRecyclerView = findViewById(R.id.rv_movie_posters_list);
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);
        mMoviePostersRecyclerView.setHasFixedSize(true);
        mMoviePostersRecyclerView.setAdapter(mMovieListAdapter);
        Log.w(TAG, "count is " + mMovieListAdapter.getItemCount());
    }

    private int calculateSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 5;
        else
            return 3;
    }
}