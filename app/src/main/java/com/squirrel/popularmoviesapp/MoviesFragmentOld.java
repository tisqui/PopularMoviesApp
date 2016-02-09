package com.squirrel.popularmoviesapp;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squirrel.popularmoviesapp.ui.MoviesFragment;
import com.squirrel.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 2/2/16.
 */
public class MoviesFragmentOld extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>>  {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private RecyclerGridViewAdapter mMoviesRecyclerGridViewAdapter;
    private RecyclerView mRecyclerViewMovies;
    private static final int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Movie> mMovies;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager;
        mMoviesRecyclerGridViewAdapter = new RecyclerGridViewAdapter(getActivity(), new ArrayList<Movie>());

        View rootView = inflater.inflate(R.layout.content_main, container, false);
//        mRecyclerViewMovies= (RecyclerView) rootView.findViewById(R.id.grid_recycler_view);
//        //Set the number of columns in the grid depending on the orientation
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
//            mRecyclerViewMovies.setLayoutManager(gridLayoutManager);
//        } else {
//            gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
//            mRecyclerViewMovies.setLayoutManager(gridLayoutManager);
//        }
//
//        mMoviesRecyclerGridViewAdapter = new RecyclerGridViewAdapter(getActivity(), new ArrayList<Movie>());
//        mRecyclerViewMovies.setAdapter(mMoviesRecyclerGridViewAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setHasOptionsMenu(true);
//
//
//        mContentResolver = getActivity().getContentResolver();
//
//        //set the default text on the screen when there is no data
//        getLoaderManager().initLoader(LOADER_ID, null, this);
//        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
//        mContentResolver = getActivity().getContentResolver();
//        return new DBDataLoader(getActivity(), MoviesContract.MoviesEntry.CONTENT_URI, mContentResolver, null);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
//        mMoviesRecyclerGridViewAdapter.updateImagesInGrid(data);
//        mMovies = data;
//        if(isResumed()){
//        } else {
//        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
//        mMoviesRecyclerGridViewAdapter.updateImagesInGrid(null);//delete the data that is in data set
    }
}
