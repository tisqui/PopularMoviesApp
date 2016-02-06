package com.squirrel.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Review;
import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.Map;
import java.util.Set;

/**
 * Created by squirrel on 1/31/16.
 */
public class TestUtil extends AndroidTestCase {

    public static final String TEST_TRAILER_ID = "123";
    public static final String TEST_REVIEW_ID = "321";
    public static final String TEST_MOVIE_ID = "test";

    static ContentValues createMovieTableValues(String movieId) {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.MOVIE_ID, movieId);
        testValues.put(MoviesContract.MoviesEntry.MOVIE_TITLE, "Mad Max");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE, "2015");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE, "7.8");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_VIDEO, "false");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_OVERVIEW, "Overview of the film");
        testValues.put(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH, "/shgdjagjhgjhsgjd.jpg");
        return testValues;
    }

    static ContentValues createTrailerTableValues(String movieId, String trailerId) {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.TrailersEntry.TRAILER_KEY, trailerId);
        testValues.put(MoviesContract.TrailersEntry.MOVIE_KEY, movieId);
        testValues.put(MoviesContract.TrailersEntry.TRAILER_KEY, "2323232323");
        testValues.put(MoviesContract.TrailersEntry.TRAILER_NAME, "First trailer");

        return testValues;
    }

    static ContentValues createReviewTableValues(String movieId, String reviewId) {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.ReviewEntry.REVIEW_ID, reviewId);
        testValues.put(MoviesContract.ReviewEntry.MOVIE_KEY, movieId);
        testValues.put(MoviesContract.ReviewEntry.REVIEW_AUTHOR, "Author");
        testValues.put(MoviesContract.ReviewEntry.REVIEW_AUTHOR, "Content");
        return testValues;
    }

    static Movie createMovie(String id){
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle("Title of the movie");
        movie.setHasVideo("true");
        movie.setPosterPath("23233.jpg");
        movie.setOverview("Overview");
        movie.setReleaseDate("2015");
        movie.setVoteAverage("7.5");
        return movie;
    }

    static Review createReview(String reviewId){
        Review review = new Review();
        review.setId(reviewId);
        review.setContent("Content");
        review.setAuthor("Author");
        return review;
    }

    static Trailer createTrailer(String trailerId){
        Trailer trailer = new Trailer();
        trailer.setId(trailerId);
        trailer.setName("Name");
        trailer.setKey("23232323");
        return  trailer;
    }

    static boolean compareMovies(Movie movie1, Movie movie2){
        return (movie1.getId().equals(movie2.getId()) &&
                movie1.getTitle().equals(movie2.getTitle()) &&
                movie1.getPosterPath().equals(movie2.getPosterPath()) &&
                movie1.getHasVideo().equals(movie2.getHasVideo()) &&
                movie1.getReleaseDate().equals(movie2.getReleaseDate()) &&
                movie1.getOverview().equals(movie2.getOverview()) &&
                movie1.getVoteAverage().equals(movie2.getVoteAverage()));
    }

    static boolean compareTrailers(Trailer trailer1, Trailer trailer2){
        return (trailer1.getId().equals(trailer2.getId()) &&
                trailer1.getKey().equals(trailer2.getKey()) &&
                trailer1.getName().equals(trailer2.getName())
                );
    }

    static boolean compareReviews(Review review1, Review review2){
        return (review1.getId().equals(review2.getId()) &&
                review1.getAuthor().equals(review2.getAuthor()) &&
                review1.getContent().equals(review2.getContent())

        );
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
