package com.squirrel.popularmoviesapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Review;
import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 2/3/16.
 */
public class DBService {
    private static final String LOG_TAG = DBService.class.getSimpleName();
    private ContentResolver mContentResolver;

    public DBService(Context context) {
        mContentResolver = context.getContentResolver();
    }


    public List<Movie> getFavoriteMovies(){
        return getMovies(null, null);
    }

    @NonNull
    private List<Movie> getMovies(String selection, String[] selectionArgs) {
        List<Movie> moviesList = new ArrayList<Movie>();
        Cursor cursor = mContentResolver.query(MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if(cursor != null){
            if(cursor.moveToFirst()) {
                do{
                    moviesList.add(DataMapper.mapMovie(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return moviesList;
    }

    public List<Trailer> getFavoriteMovieTrailers(String movieId){
        List<Trailer> trailerList = new ArrayList<Trailer>();
        String[] selectionArgs = new String[]{ movieId };
        Cursor cursor = mContentResolver.query(MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                MoviesContract.TrailersEntry.MOVIE_KEY + "=?",
                selectionArgs,
                null);
        if(cursor != null){
            if(cursor.moveToFirst()) {
                do{
                    trailerList.add(DataMapper.mapTrailer(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return trailerList;
    }

    public List<Review> getFavoriteMovieReviews(String movieId){
        List<Review> reviewList = new ArrayList<Review>();
        String[] selectionArgs = new String[]{ movieId };
        Cursor cursor = mContentResolver.query(MoviesContract.ReviewEntry.CONTENT_URI,
                null,
                MoviesContract.ReviewEntry.MOVIE_KEY+ "=?",
                selectionArgs,
                null);
        if(cursor != null){
            if(cursor.moveToFirst()) {
                do{
                    reviewList.add(DataMapper.mapReview(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return reviewList;
    }

    public void saveToFavorites(Movie movie, List<Trailer> trailers, List<Review> reviews){
        ContentValues movieValues = DataMapper.mapMovieToValues(movie);
        mContentResolver.insert(MoviesContract.MoviesEntry.CONTENT_URI, movieValues);

        if (trailers != null) {
            for(Trailer trailer : trailers){
                ContentValues trailerValues = DataMapper.mapTrailerToValues(trailer, movie.getId());
                mContentResolver.insert(MoviesContract.TrailersEntry.CONTENT_URI, trailerValues);
            }
        }

        if (reviews != null) {
            for(Review review : reviews){
                ContentValues reviewValues = DataMapper.mapReviewToValues(review, movie.getId());
                mContentResolver.insert(MoviesContract.ReviewEntry.CONTENT_URI, reviewValues);
            }
        }
    }

    public void deleteFromFavorites(String movieId){

        mContentResolver.delete(MoviesContract.MoviesEntry.CONTENT_URI,
                MoviesContract.MoviesEntry.MOVIE_ID + " = ? ",
                new String[] {movieId});

        mContentResolver.delete(MoviesContract.TrailersEntry.CONTENT_URI,
                MoviesContract.TrailersEntry.MOVIE_KEY + " = ? ",
                new String[]{movieId});

        mContentResolver.delete(MoviesContract.ReviewEntry.CONTENT_URI,
                MoviesContract.ReviewEntry.MOVIE_KEY + " = ? ",
                new String[] {movieId});
    }

    public void deleteAllMoviesFromFavorites(){
        mContentResolver.delete(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null
        );
        mContentResolver.delete(
                MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                null
        );
        mContentResolver.delete(
                MoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );
    }

    public boolean isFavorite(String movieId){
        List<Movie> result = getMovies(MoviesContract.MoviesEntry.MOVIE_ID + "=?", new String[] {movieId});
        if(result.isEmpty()){
            return false;
        } else {
            return true;
        }
    }



}
