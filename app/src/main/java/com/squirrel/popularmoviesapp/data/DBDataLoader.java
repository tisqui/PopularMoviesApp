package com.squirrel.popularmoviesapp.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.squirrel.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 2/2/16.
 */
public class DBDataLoader extends AsyncTaskLoader<List<Movie>> {


    private List<Movie> mDataList;
    private Uri mUri;
    private String[] mProjection;
    private static final String LOG_TAG = DBDataLoader.class.getSimpleName();
    private ContentResolver mContentResolver;
    private Cursor mCursor;


    public DBDataLoader(Context context, Uri uri, ContentResolver contentResolver, String[] projection) {
        super(context);
        mContentResolver = contentResolver;
        mUri = uri;
        mProjection = projection;
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> listOfData = new ArrayList<Movie>();
        mCursor = mContentResolver.query(mUri, mProjection, null, null, null);
        Movie initialMovie = new Movie();
        initialMovie.setId("454756");
        initialMovie.setTitle("Title");
        initialMovie.setReleaseDate("4545");
        initialMovie.setOverview("Overview");
        initialMovie.setVoteAverage("76");
        initialMovie.setHasVideo("");
        initialMovie.setPosterPath("\\/3ktww154vChp5Y1qOrJZFLvqYz8.jpg");

        listOfData.add(initialMovie);
        listOfData.add(initialMovie);
        listOfData.add(initialMovie);

        if( mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    Movie movie = new Movie();
                    movie.setId(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID))));
                    movie.setTitle(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_TITLE)));
                    movie.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE)));
                    movie.setOverview(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_OVERVIEW)));
                    movie.setVoteAverage(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE)));
                    movie.setHasVideo(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VIDEO)));
                    movie.setPosterPath(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_POSTER_PATH)));
                    listOfData.add(movie);
                } while (mCursor.moveToNext());
            }
        }
        return listOfData;
    }



    @Override
    public void deliverResult(List<Movie> data) {
        //check if data was reset
        if(isReset()){
            //if no data was passed, just close
            if(data != null){
                mCursor.close();
            }
        }
        List<Movie> oldListOfMovies = mDataList;
        if(mDataList == null || mDataList.size() == 0){
            Log.d(LOG_TAG, "There is no data returned!!!");
        }
        mDataList = data;
        if(isStarted()){
            super.deliverResult(data);
        }
        //the new data is different from the previous one
        if(oldListOfMovies != null && oldListOfMovies != data){
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mDataList != null){
            //data is set and processed
            deliverResult(mDataList);
        }

        if(takeContentChanged() || mDataList == null){
            //if the data is changed when loader was stopped
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        //loader is stopping to load the data
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null){
            mCursor.close();
        }
        mDataList = null; //so next time on start we will load data again
    }

    @Override
    public void onCanceled(List<Movie> data) {
        super.onCanceled(data);
        if(mCursor != null){
            mCursor.close();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
