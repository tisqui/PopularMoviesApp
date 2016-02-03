package com.squirrel.popularmoviesapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by squirrel on 1/31/16.
 */
public class MoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDB mMoviesDB;

    static final int MOVIES = 100;
    static final int TRAILERS = 200;
    static final int TRAILERS_WITH_MOVIES = 201;

    static final int REVIEWS = 300;
    static final int REVIEWS_WITH_MOVIES = 301;

    private static final SQLiteQueryBuilder sTrailersByMovieQueryBuilder;
    private static final SQLiteQueryBuilder sReviewsByMovieQueryBuilder;

    static{
        sTrailersByMovieQueryBuilder = new SQLiteQueryBuilder();
        sReviewsByMovieQueryBuilder = new SQLiteQueryBuilder();

        //trailers INNER JOIN movies ON trailers.movie_id = movies._id
        sTrailersByMovieQueryBuilder.setTables(
                MoviesDB.Tables.TRAILERS + " INNER JOIN " +
                        MoviesDB.Tables.MOVIES +
                        " ON " + MoviesDB.Tables.TRAILERS +
                        "." + MoviesContract.TrailersEntry.MOVIE_KEY +
                        " = " + MoviesDB.Tables.MOVIES +
                        "." + MoviesContract.MoviesEntry.MOVIE_ID);

        //reviews INNER JOIN movies ON reviews.movie_id = movies._id
        sReviewsByMovieQueryBuilder.setTables(
                MoviesDB.Tables.REVIEWS + " INNER JOIN " +
                        MoviesDB.Tables.MOVIES +
                        " ON " + MoviesDB.Tables.REVIEWS +
                        "." + MoviesContract.ReviewEntry.MOVIE_KEY +
                        " = " + MoviesDB.Tables.MOVIES +
                        "." + MoviesContract.MoviesEntry.MOVIE_ID);
    }

    private static final String sTrailerMovieSettings =
            MoviesDB.Tables.TRAILERS+
                    "." + MoviesContract.TrailersEntry.MOVIE_KEY + " = ? ";

    private static final String sReviewMovieSettings =
            MoviesDB.Tables.REVIEWS+
                    "." + MoviesContract.ReviewEntry.MOVIE_KEY + " = ? ";


    public static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(authority, MoviesContract.PATH_REVIEWS, REVIEWS);
        uriMatcher.addURI(authority, MoviesContract.PATH_TRAILERS, TRAILERS);
        uriMatcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/*", REVIEWS_WITH_MOVIES);
        uriMatcher.addURI(authority, MoviesContract.PATH_TRAILERS + "/*", TRAILERS_WITH_MOVIES);
        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        mMoviesDB = new MoviesDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "reviews/*"
            case REVIEWS_WITH_MOVIES:
            {
                retCursor = getReviewsByMovie(uri, projection, sortOrder);
                break;
            }
            // "trailers/*"
            case TRAILERS_WITH_MOVIES: {
               retCursor = getTrailersByMovie(uri, projection, sortOrder);
                break;
            }
            // "movies"
            case MOVIES: {
                retCursor = mMoviesDB.getReadableDatabase().query(
                        MoviesDB.Tables.MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS: {
                retCursor = mMoviesDB.getReadableDatabase().query(
                        MoviesDB.Tables.TRAILERS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEWS: {
                retCursor = mMoviesDB.getReadableDatabase().query(
                        MoviesDB.Tables.REVIEWS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getTrailersByMovie(
            Uri uri, String[] projection, String sortOrder){
        return sTrailersByMovieQueryBuilder.query(mMoviesDB.getReadableDatabase(),
                projection,
                sTrailerMovieSettings,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewsByMovie(
            Uri uri, String[] projection, String sortOrder){
        return sReviewsByMovieQueryBuilder.query(mMoviesDB.getReadableDatabase(),
                projection,
                sReviewMovieSettings,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case TRAILERS:
                return MoviesContract.TrailersEntry.CONTENT_TYPE;
            case REVIEWS:
                return MoviesContract.ReviewEntry.CONTENT_TYPE;
            case TRAILERS_WITH_MOVIES:
                return MoviesContract.TrailersEntry.CONTENT_TYPE;
            case REVIEWS_WITH_MOVIES:
                return MoviesContract.ReviewEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMoviesDB.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MoviesDB.Tables.MOVIES, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.MoviesEntry.buildMovieUri(String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS: {
                long _id = db.insert(MoviesDB.Tables.TRAILERS, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.TrailersEntry.buildTrailerUri(String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {
                long _id = db.insert(MoviesDB.Tables.REVIEWS, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.ReviewEntry.buildReviewUri(String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMoviesDB.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDB.Tables.MOVIES, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case TRAILERS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDB.Tables.TRAILERS, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case REVIEWS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDB.Tables.REVIEWS, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private void deleteDB(){
        mMoviesDB.close();
        MoviesDB.deleteDatabase(getContext());
        mMoviesDB = new MoviesDB(getContext());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDB.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deleted = 0;
        switch(match){
            case MOVIES:{
                deleted = db.delete(MoviesDB.Tables.MOVIES, selection, selectionArgs);
                if(deleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            }
            case REVIEWS:{
                deleted = db.delete(MoviesDB.Tables.REVIEWS, selection, selectionArgs);
                if(deleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            }
            case TRAILERS:{
                deleted = db.delete(MoviesDB.Tables.TRAILERS, selection, selectionArgs);
                if(deleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mMoviesDB.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int updated = 0;

            switch(match){
                case MOVIES:{
                    updated= db.update(MoviesDB.Tables.MOVIES, values, selection, selectionArgs);

                    break;
                }
                case TRAILERS:{
                    updated = db.update(MoviesDB.Tables.TRAILERS, values, selection, selectionArgs);
                    break;
                }
                case REVIEWS:{
                    updated = db.update(MoviesDB.Tables.REVIEWS, values, selection, selectionArgs);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            if(updated > 0){
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return updated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mMoviesDB.close();
        super.shutdown();
    }
}
