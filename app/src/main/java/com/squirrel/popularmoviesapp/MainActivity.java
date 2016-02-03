package com.squirrel.popularmoviesapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squirrel.popularmoviesapp.API.MoviesAPIService;
import com.squirrel.popularmoviesapp.data.DBDataLoader;
import com.squirrel.popularmoviesapp.data.MoviesContract;
import com.squirrel.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerGridViewAdapter mRecyclerGridViewAdapter;
    private String mSortOrder;
    private final String MOVIES_FRAGMENT_TAG = "FFTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToolbar();

        mSortOrder = getString(R.string.order_setting_default_value);

        //Create RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);

        //Set the number of columns in the grid depending on the orientation
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        mRecyclerGridViewAdapter = new RecyclerGridViewAdapter(MainActivity.this, new ArrayList<Movie>());
        mRecyclerView.setAdapter(mRecyclerGridViewAdapter);

        //set the on touch listener for the RecyclerView. The same action for tap and long tap
        mRecyclerView.addOnItemTouchListener(new MovieClickListener(this, mRecyclerView,
                new MovieClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        intent.putExtra(FILM_DETAILS_KEY, mRecyclerGridViewAdapter.getImage(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        intent.putExtra(FILM_DETAILS_KEY, mRecyclerGridViewAdapter.getImage(position));
                        startActivity(intent);
                    }
                }));

        //set the scroll listener for the recycler view
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                mSortOrder = sharedPref.getString(getString(R.string.order_key_text), getString(R.string.order_setting_default_value));

                if (mSortOrder.equals("favorites")) {
                    //load the data from DB
                } else {

                    MoviesAPIService apiService = new MoviesAPIService(BuildConfig.MY_API_KEY);
                    apiService.getMovies(mSortOrder, Integer.toString(page), new MoviesAPIService.APICallback<List<Movie>>() {
                        @Override
                        public void onSuccess(List<Movie> result) {
                            mRecyclerGridViewAdapter.addImagesToGrid(result);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(LOG_TAG, t.getLocalizedMessage());
                            Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    /**
     * Update the RecyclerView data when the settings were changed
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerGridViewAdapter != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            mSortOrder = sharedPref.getString(getString(R.string.order_key_text), getString(R.string.order_setting_default_value));

            if (mSortOrder.equals("favorites")) {
                //load the data from DB
                ContentResolver contentResolver = this.getContentResolver();
                DBDataLoader loader = new DBDataLoader(this, MoviesContract.MoviesEntry.CONTENT_URI,contentResolver, null);
                List<Movie> movies = loader.loadInBackground();
                if(movies != null){
                    mRecyclerGridViewAdapter.updateImagesInGrid(movies);
                }

            } else {

                String page = "1";
                MoviesAPIService apiService = new MoviesAPIService(BuildConfig.MY_API_KEY);
                apiService.getMovies(mSortOrder, page, new MoviesAPIService.APICallback<List<Movie>>() {
                    @Override
                    public void onSuccess(List<Movie> result) {
                        mRecyclerGridViewAdapter.updateImagesInGrid(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(LOG_TAG, t.getLocalizedMessage());
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
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
