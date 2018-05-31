package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviePostersRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create the GridLayoutManager for the recycler view, set span to 2
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);

        // create the Adapter
        mMovieListAdapter = new MovieListAdapter();

        // attach the Recycler view and set its attributes
        mMoviePostersRecyclerView = findViewById(R.id.rv_movie_posters_list);
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);
        mMoviePostersRecyclerView.setHasFixedSize(true);
        mMoviePostersRecyclerView.setAdapter(mMovieListAdapter);
    }

}

