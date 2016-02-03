package com.squirrel.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by squirrel on 1/31/16.
 */
public class TestUtil extends AndroidTestCase {

    static ContentValues createMovieTableValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.MOVIE_ID, "test");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_TITLE, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE, "2015");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE, "7.8");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_VIDEO, "false");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_OVERVIEW, "Overview of the film");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH, "/shgdjagjhgjhsgjd.jpg");
        return testValues;
    }

    static ContentValues createTrailerTableValues(long movieId) {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.TrailersEntry.MOVIE_KEY, movieId);
        testValues.put(MoviesContract.TrailersEntry.TRAILER_KEY, "2323232323");
        testValues.put(MoviesContract.TrailersEntry.TRAILER_NAME, "First trailer");

        return testValues;
    }

    static ContentValues createReviewTableValues(long movieId) {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.ReviewEntry.MOVIE_KEY, movieId);
        testValues.put(MoviesContract.ReviewEntry.REVIEW_AUTHOR, "Author");
        testValues.put(MoviesContract.ReviewEntry.REVIEW_AUTHOR, "Content");
        return testValues;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String valueFromCursor = valueCursor.getString(idx);

            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueFromCursor);
        }
    }

    /*
        Class taken from the Udacity course lectures for testing: The functions we provide inside of
        TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
