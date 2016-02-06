package com.squirrel.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Review;
import com.squirrel.popularmoviesapp.model.Trailer;

/**
 * Created by squirrel on 2/3/16.
 */
public class DataMapper {
    private static final String LOG_TAG = DataMapper.class.getSimpleName();
    
    public static Movie mapMovie(Cursor cursor){
        Movie movie = new Movie();
        movie.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_TITLE)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_OVERVIEW)));
        movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE)));
        movie.setHasVideo(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VIDEO)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_POSTER_PATH)));
        return movie;
    }

    public static Trailer mapTrailer(Cursor cursor){
        Trailer trailer = new Trailer();
        trailer.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.TrailersColumns.TRAILER_ID)));
        trailer.setKey(cursor.getString(cursor.getColumnIndex(MoviesContract.TrailersColumns.TRAILER_KEY)));
        trailer.setName(cursor.getString(cursor.getColumnIndex(MoviesContract.TrailersColumns.TRAILER_NAME)));
        return trailer;
    }

    public static Review mapReview(Cursor cursor){
        Review review = new Review();
        review.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.ReviewsColumns.REVIEW_ID)));
        review.setAuthor(cursor.getString(cursor.getColumnIndex(MoviesContract.ReviewsColumns.REVIEW_AUTHOR)));
        review.setContent(cursor.getString(cursor.getColumnIndex(MoviesContract.ReviewsColumns.REVIEW_CONTENT)));
        return review;
    }
    
    public static ContentValues mapMovieToValues(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_ID, movie.getId());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_TITLE, movie.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_VIDEO, movie.getHasVideo());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH, movie.getPosterPath());
        return contentValues;
    }
    
    public static ContentValues mapTrailerToValues(Trailer trailer, String movieId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.TrailersEntry.TRAILER_ID, trailer.getId());
        contentValues.put(MoviesContract.TrailersEntry.MOVIE_KEY, movieId);
        contentValues.put(MoviesContract.TrailersEntry.TRAILER_KEY, trailer.getKey());
        contentValues.put(MoviesContract.TrailersEntry.TRAILER_NAME, trailer.getName());

        return contentValues;
    }
    
    public static ContentValues mapReviewToValues(Review review, String movieId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.ReviewEntry.REVIEW_ID, review.getId());
        contentValues.put(MoviesContract.ReviewEntry.MOVIE_KEY, movieId);
        contentValues.put(MoviesContract.ReviewEntry.REVIEW_AUTHOR, review.getAuthor());
        contentValues.put(MoviesContract.ReviewEntry.REVIEW_CONTENT, review.getContent());
        return contentValues;
    }
    
}
