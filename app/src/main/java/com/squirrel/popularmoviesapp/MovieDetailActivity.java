package com.squirrel.popularmoviesapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
   //on Details screen there will be only action items from fragment
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Activity does not have any actions
                return true;
            case R.id.action_share:
                // Not implemented here
                return false;
            default:
                break;
        }
        return false;
    }

}
