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
    private boolean isFirstLaunch = true; //the flag to define if the app was launched for the first time


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
            // The application is in two pane mode
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
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
                setDetailsFragmentForTablet(movie);
            }
        }else {
            if (mTwoPane) {
                setDetailsFragmentForTablet(movie);
            } else {
                Intent intent = new Intent(this, MovieDetailActivity.class);
                intent.putExtra(BaseActivity.FILM_DETAILS_KEY, movie);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // Activity action
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_share:
                // Not implemented here, this is the fragment action in 2-pane mode
                return false;
            default:
                break;
        }
        return false;
    }

    private void setDetailsFragmentForTablet(Movie movie){
        Bundle args = new Bundle();
        args.putSerializable(BaseActivity.FILM_DETAILS_KEY, movie);

        //pass the movie object to the fragment
        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        detailsFragment.setArguments(args);

        //start fragment transaction
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_detail, detailsFragment, DETAILFRAGMENT_TAG)
                .commit();
    }

}
