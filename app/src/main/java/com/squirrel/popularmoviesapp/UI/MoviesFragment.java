package com.squirrel.popularmoviesapp.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squirrel.popularmoviesapp.BuildConfig;
import com.squirrel.popularmoviesapp.EndlessRecyclerViewScrollListener;
import com.squirrel.popularmoviesapp.MovieClickListener;
import com.squirrel.popularmoviesapp.R;
import com.squirrel.popularmoviesapp.RecyclerGridViewAdapter;
import com.squirrel.popularmoviesapp.api.MoviesAPIService;
import com.squirrel.popularmoviesapp.data.DBService;
import com.squirrel.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 2/8/16.
 */
public class MoviesFragment extends Fragment{
    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerGridViewAdapter mRecyclerGridViewAdapter;
    private String mSortOrder;
    public DBService mDBService;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager;
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSortOrder = getString(R.string.order_setting_default_value);

        mDBService = new DBService(getActivity().getApplicationContext());

        //Create RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.grid_recycler_view);

        //Set the number of columns in the grid depending on the orientation
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        mRecyclerGridViewAdapter = new RecyclerGridViewAdapter(getActivity().getApplicationContext(), new ArrayList<Movie>());
        mRecyclerView.setAdapter(mRecyclerGridViewAdapter);

        //set the on touch listener for the RecyclerView. The same action for tap and long tap
        mRecyclerView.addOnItemTouchListener(new MovieClickListener(getActivity().getApplicationContext(), mRecyclerView,
                new MovieClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                            ((Callback) getActivity())
                                    .onItemSelected(mRecyclerGridViewAdapter.getImage(position));
                        mPosition = position;

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        ((Callback) getActivity())
                                .onItemSelected(mRecyclerGridViewAdapter.getImage(position));
                        mPosition = position;
                    }
                }));

        // If there's instance state, get the last selected position
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        //set the scroll listener for the recycler view
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                mSortOrder = sharedPref.getString(getString(R.string.order_key_text), getString(R.string.order_setting_default_value));

                if (mSortOrder.equals("favorites")) {
                    //no load more for movies from DB, all films are loaded at once
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
                            Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRecyclerGridViewAdapter != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            mSortOrder = sharedPref.getString(getString(R.string.order_key_text), getString(R.string.order_setting_default_value));

            if (mSortOrder.equals("favorites")) {
                //load the data from DB
                List<Movie> moviesFromDB = mDBService.getFavoriteMovies();
                if(moviesFromDB.isEmpty() || moviesFromDB ==null){
                    Toast.makeText(getActivity().getApplicationContext(), "There are no movies in Favorites", Toast.LENGTH_LONG).show();
                } else{
                    mRecyclerGridViewAdapter.addImagesToGrid(moviesFromDB);
                }

            } else {
                String page = getString(R.string.first_page_movies_api);

                //get movies from API
                MoviesAPIService apiService = new MoviesAPIService(BuildConfig.MY_API_KEY);
                apiService.getMovies(mSortOrder, page, new MoviesAPIService.APICallback<List<Movie>>() {
                    @Override
                    public void onSuccess(List<Movie> result) {
                        mRecyclerGridViewAdapter.updateImagesInGrid(result);
                        Log.d(LOG_TAG, "" + result.size());
                        for (Movie item : result) {
                            Log.d(LOG_TAG, item.toString());
                        }
                        //if this is the first show of the Detail view
                        if (mPosition == RecyclerView.NO_POSITION){
                            ((Callback) getActivity())
                                    .onItemSelected(mRecyclerGridViewAdapter.getImage(0));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(LOG_TAG, t.getLocalizedMessage());
                        Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (mPosition != RecyclerView.NO_POSITION) {
                //when the movie item was shown previously
                mRecyclerView.smoothScrollToPosition(mPosition);
            }
        }
    }

}
