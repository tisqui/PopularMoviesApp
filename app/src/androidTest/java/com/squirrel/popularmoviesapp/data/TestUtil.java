package com.squirrel.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by squirrel on 1/31/16.
 */
public class TestUtil extends AndroidTestCase {

    static ContentValues createMovieTableValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.MOVIE_TITLE, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_VIDEO, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_OVERVIEW, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH, "Mad Max");
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
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
