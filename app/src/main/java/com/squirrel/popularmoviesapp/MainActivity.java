package com.squirrel.popularmoviesapp;

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
import com.squirrel.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerGridViewAdapter mRecyclerGridViewAdapter;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToolbar();

        mSortOrder = getString(R.string.order_setting_default_value);

        //Create RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);

        //Set the number of columns in the grid depending on the orientation
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        }

        mRecyclerGridViewAdapter = new RecyclerGridViewAdapter(MainActivity.this, new ArrayList<Movie>());
//        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
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

        //TEST RETROFIT
//        String BASE_URL = "http://api.themoviedb.org/3/";
//
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        MoviesAPI apiService = retrofit.create(MoviesAPI.class);
//        String sortBy = "popularity.desc";
//        String API_KEY = "3db5ceb6c6b91619aff5af60a60a70cc";
//        String page = "1";
//
//        Call<ResponseWrapper<Movie>> call = apiService.getMovies(sortBy, page, API_KEY);
//
//        call.enqueue(new Callback<ResponseWrapper<Movie>>() {
//            @Override
//            public void onResponse(Response<ResponseWrapper<Movie>> response) {
//                int statusCode = response.code();
//                List<Movie> movies = response.body().getResults();
//
//                String res="";
//                for(Movie movie : movies){
//                    res += movie.toString()+"\n";
//                }
//                Toast.makeText(MainActivity.this,"RESULTS:"+ res`, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Toast.makeText(MainActivity.this,"NOT WORKING BITCH!!!!" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });



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
