package com.squirrel.popularmoviesapp.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.AndroidTestCase;

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

}
