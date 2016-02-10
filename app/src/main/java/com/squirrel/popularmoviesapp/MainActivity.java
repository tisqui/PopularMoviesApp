package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.squirrel.popularmoviesapp.data.DBService;
import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.ui.MovieDetailsFragment;
import com.squirrel.popularmoviesapp.ui.MoviesFragment;

public class MainActivity extends BaseActivity implements MoviesFragment.Callback{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public DBService mDBService;
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean isFirstLaunch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToolbar();

        mDBService = new DBService(this);

        MoviesFragment moviesFragment = ((MoviesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movies_list));

        if (findViewById(R.id.fragment_movie_detail) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            if (savedInstanceState == null) {
                //check if need to do anything
            }
        } else {
            mTwoPane = false;
        }
    }

    /**
     * Update the data
     */
    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onItemSelected(Movie movie) {
        if(isFirstLaunch){
            isFirstLaunch = false;
            if(mTwoPane){
                Bundle args = new Bundle();
                args.putSerializable(BaseActivity.FILM_DETAILS_KEY, movie);

                MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
                detailsFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movie_detail, detailsFragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        }else {
            if (mTwoPane) {
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                Bundle args = new Bundle();
                args.putSerializable(BaseActivity.FILM_DETAILS_KEY, movie);

                MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
                detailsFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movie_detail, detailsFragment, DETAILFRAGMENT_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, MovieDetailActivity.class);
                intent.putExtra(BaseActivity.FILM_DETAILS_KEY, movie);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Movie getTestMovie(){
        Movie movie = new Movie();
        movie.setId("112133343453453");
        movie.setTitle("Title of the movie");
        movie.setHasVideo("true");
        movie.setPosterPath("23233.jpg");
        movie.setOverview("Overview");
        movie.setReleaseDate("2015");
        movie.setVoteAverage("7.5");
        return movie;
    }

}
