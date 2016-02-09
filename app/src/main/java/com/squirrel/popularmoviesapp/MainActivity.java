package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.squirrel.popularmoviesapp.data.DBService;
import com.squirrel.popularmoviesapp.ui.MoviesFragment;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public DBService mDBService;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToolbar();

        mDBService = new DBService(this);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
//                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        MoviesFragment moviesFragment = ((MoviesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movies_list));

    }

    /**
     * Update the RecyclerView data when the settings were changed
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

}
