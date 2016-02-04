package com.squirrel.popularmoviesapp.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by squirrel on 2/1/16.
 */
public class TestContentProvider extends AndroidTestCase{
    public static final String LOG_TAG = TestContentProvider.class.getSimpleName();

    public void deleteAllRecordsFromContentProvider() {
        mContext.getContentResolver().delete(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movies table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Trailers table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Trailers table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {

        deleteAllRecordsFromContentProvider();
    }

    // Strart each test to start with a clean slate, run deleteAllRecords
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MoviesProvider.class.getName());
        try {

            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);

        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        // content://com.squirrel.popularmoviesapp/movies/
        String type = mContext.getContentResolver().getType(MoviesContract.MoviesEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.squirrel.popularmoviesapp/movies
        assertEquals("Error: the MoviesEntry CONTENT_URI should return MoviesEntry.CONTENT_TYPE",
                MoviesContract.MoviesEntry.CONTENT_TYPE, type);

        // content://com.squirrel.popularmoviesapp/trailers/
        type = mContext.getContentResolver().getType(MoviesContract.TrailersEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.squirrel.popularmoviesapp/trailers
        assertEquals("Error: the TrailersEntry CONTENT_URI should return TrailersEntry.CONTENT_TYPE",
                MoviesContract.TrailersEntry.CONTENT_TYPE, type);

        // content://com.squirrel.popularmoviesapp/reviews/
        type = mContext.getContentResolver().getType(MoviesContract.ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.squirrel.popularmoviesapp/reviews
        assertEquals("Error: the MoviesEntry CONTENT_URI should return MoviesEntry.CONTENT_TYPE",
                MoviesContract.ReviewEntry.CONTENT_TYPE, type);

        String testMovie = "01";
        // content://com.squirrel.popularmoviesapp/trailers/01
        type = mContext.getContentResolver().getType(
                MoviesContract.TrailersEntry.buildTrailerMovie(testMovie));
        // vnd.android.cursor.dir/com.squirrel.popularmoviesapp/trailers
        assertEquals("Error: the TrailerEntry CONTENT_URI with movie should return TrailersEntry.CONTENT_TYPE",
                MoviesContract.TrailersEntry.CONTENT_TYPE, type);

        // content://com.squirrel.popularmoviesapp/reviews/01
        type = mContext.getContentResolver().getType(
                MoviesContract.ReviewEntry.buildReviewMovie(testMovie));
        // vnd.android.cursor.dir/com.squirrel.popularmoviesapp/reviews
        assertEquals("Error: theReviewEntry CONTENT_URI with movie should return ReviewEntry.CONTENT_TYPE",
                MoviesContract.ReviewEntry.CONTENT_TYPE, type);

    }

    public void testBasicTrailersQuery() {
        MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();

        ContentValues testValues = TestUtil.createMovieTableValues(TestUtil.TEST_MOVIE_ID);
        long movieId = db.insert(MoviesDB.Tables.MOVIES, null, testValues);

        ContentValues trailersValues = TestUtil.createTrailerTableValues(TestUtil.TEST_MOVIE_ID, TestUtil.TEST_TRAILER_ID);

        long trailerId = db.insert(MoviesDB.Tables.TRAILERS, null, trailersValues);
        assertTrue("Unable to Insert TrailersEntry into the Database", trailerId != -1);

        db.close();

        Cursor trailersCursor = mContext.getContentResolver().query(
                MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtil.validateCursor("testBasicTrailersQuery", trailersCursor, trailersValues);
    }

    public void testBasicMoviesQueries() {
        MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();

        ContentValues testValues = TestUtil.createMovieTableValues(TestUtil.TEST_MOVIE_ID);
        long moviesRowId = db.insert(MoviesDB.Tables.MOVIES, null, testValues);;

        Cursor moviesCursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtil.validateCursor("testBasicMoviesQueries, movies query", moviesCursor, testValues);

        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: Location Query did not properly set NotificationUri",
                    moviesCursor.getNotificationUri(), MoviesContract.MoviesEntry.CONTENT_URI);
        }
    }

    public void testBasicReviewsQuery() {
        MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();

        ContentValues testValues = TestUtil.createMovieTableValues(TestUtil.TEST_MOVIE_ID);
        long movieId = db.insert(MoviesDB.Tables.MOVIES, null, testValues);

        ContentValues reviewsValues = TestUtil.createReviewTableValues(TestUtil.TEST_MOVIE_ID, TestUtil.TEST_REVIEW_ID);

        long reviewId = db.insert(MoviesDB.Tables.REVIEWS, null,reviewsValues);
        assertTrue("Unable to Insert TrailersEntry into the Database", reviewId != -1);

        db.close();

        Cursor reviewsCursor = mContext.getContentResolver().query(
                MoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtil.validateCursor("testBasicTrailersQuery", reviewsCursor, reviewsValues);
    }

    public void testUpdateMovies() {
        ContentValues values = TestUtil.createMovieTableValues(TestUtil.TEST_MOVIE_ID);

        Uri moviesUri = mContext.getContentResolver().
                insert(MoviesContract.MoviesEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(moviesUri);

        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New row id: " + movieRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MoviesContract.MoviesEntry.MOVIE_ID, movieRowId);
        updatedValues.put(MoviesContract.MoviesEntry.MOVIE_TITLE, "Super new film");

        Cursor movieCursor = mContext.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);

        TestUtil.TestContentObserver tco = TestUtil.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MoviesContract.MoviesEntry.CONTENT_URI, updatedValues, MoviesContract.MoviesEntry.MOVIE_ID + "= ?",
                new String[] {"test"});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,   // projection
                MoviesContract.MoviesEntry.MOVIE_ID + " = " + movieRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtil.validateCursor("testUpdateLocation.  Error validating movies entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtil.createMovieTableValues(TestUtil.TEST_MOVIE_ID);

        TestUtil.TestContentObserver tco = TestUtil.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, tco);
        Uri moviesUri = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long moviesRowId = ContentUris.parseId(moviesUri);

        assertTrue(moviesRowId != -1);

        //check the data
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtil.validateCursor("testInsertReadProvider. Error validating MoviesEntry.",
                cursor, testValues);

        // Movie is added. add trailers

        ContentValues trailersValues = TestUtil.createTrailerTableValues(TestUtil.TEST_MOVIE_ID, TestUtil.TEST_TRAILER_ID);
        tco = TestUtil.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MoviesContract.TrailersEntry.CONTENT_URI, true, tco);

        Uri trailerInsertUri = mContext.getContentResolver()
                .insert(MoviesContract.TrailersEntry.CONTENT_URI, trailersValues);
        assertTrue(trailerInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor trailersCursor = mContext.getContentResolver().query(
                MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtil.validateCursor("testInsertReadProvider. Error validating TrailersEntry insert.",
                trailersCursor, trailersValues);

      //Test getting the trailers and by Movie_key

    }

}
