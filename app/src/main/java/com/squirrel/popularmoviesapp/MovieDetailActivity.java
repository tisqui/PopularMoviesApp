package com.squirrel.popularmoviesapp;

import android.os.Bundle;

import com.squirrel.popularmoviesapp.ui.MovieDetailsFragment;

public class MovieDetailActivity extends BaseActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        activateToolbarWithHomeEnabled();

            MovieDetailsFragment movieDetailFragment = ((MovieDetailsFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_movie_detail));

    }

}
