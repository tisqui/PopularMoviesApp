package com.squirrel.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by squirrel on 1/31/16.
 */
public class MoviesDB extends SQLiteOpenHelper {

    private static final String LOG_TAG = MoviesDB.class.getSimpleName();
    static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;
    private Context mContext;

    interface Tables {
        String MOVIES = "movies";
        String TRAILERS = "trailers";
        String REVIEWS = "reviews";
    }

    public MoviesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + Tables.MOVIES + " (" +
                MoviesContract.MoviesEntry.MOVIE_ID + " TEXT PRIMARY KEY," +
                MoviesContract.MoviesEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE + " TEXT, " +
                MoviesContract.MoviesEntry.MOVIE_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE + " TEXT, " +
                MoviesContract.MoviesEntry.MOVIE_VIDEO + " TEXT, " +
                MoviesContract.MoviesEntry.MOVIE_POSTER_PATH + " TEXT); ";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + Tables.TRAILERS + " (" +
                MoviesContract.TrailersEntry.TRAILER_ID + " TEXT PRIMARY KEY," +
                MoviesContract.TrailersEntry.MOVIE_KEY + " TEXT NOT NULL, " +
                MoviesContract.TrailersEntry.TRAILER_KEY + " TEXT NOT NULL, " +
                MoviesContract.TrailersEntry.TRAILER_NAME + " TEXT, " +

                // Set up the movie_key column as a foreign key to movies table.
                " FOREIGN KEY (" + MoviesContract.TrailersEntry.MOVIE_KEY + ") REFERENCES " +
                        Tables.MOVIES + " (" + MoviesContract.MoviesEntry.MOVIE_ID + "));";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + Tables.REVIEWS + " (" +
                MoviesContract.ReviewEntry.REVIEW_ID + " TEXT PRIMARY KEY," +
                MoviesContract.ReviewEntry.MOVIE_KEY + " TEXT NOT NULL, " +
                MoviesContract.ReviewEntry.REVIEW_AUTHOR + " TEXT NOT NULL, " +
                MoviesContract.ReviewEntry.REVIEW_CONTENT + " TEXT, " +

                // Set up the movie_key column as a foreign key to movies table.
                " FOREIGN KEY (" + MoviesContract.ReviewEntry.MOVIE_KEY + ") REFERENCES " +
                Tables.MOVIES + " (" + MoviesContract.MoviesEntry.MOVIE_ID + "));";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion; //version the user is running now
        if(version == 1){
            //do not delete existing data, add fields - take care about the new version
            version  = 2;
        }
        if(version != DATABASE_VERSION){
            //worst case scenario
            db.execSQL("DROP TABLE IF EXISTS " + Tables.MOVIES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.TRAILERS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.REVIEWS);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
