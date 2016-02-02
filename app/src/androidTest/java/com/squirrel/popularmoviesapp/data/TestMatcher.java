package com.squirrel.popularmoviesapp.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by squirrel on 2/1/16.
 */
public class TestMatcher extends AndroidTestCase {

    static final String movieId = "1";

    private static final Uri TEST_MOVIES_DIR = MoviesContract.MoviesEntry.CONTENT_URI;
    private static final Uri TEST_TRAILERS_DIR = MoviesContract.TrailersEntry.CONTENT_URI;
    private static final Uri TEST_REVIEWS_DIR = MoviesContract.ReviewEntry.CONTENT_URI;

    private static final Uri TEST_REVIEWS_WITH_MOVIES_DIR = MoviesContract.ReviewEntry.buildReviewMovie(movieId);
    private static final Uri TEST_TRAILERS_WITH_MOVIES_DIR = MoviesContract.TrailersEntry.buildTrailerMovie(movieId);


    public void testUriMatcher() {
        UriMatcher testMatcher = MoviesProvider.buildUriMatcher();

        assertEquals("Error: The MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIES_DIR), MoviesProvider.MOVIES);
        assertEquals("Error: The TRAILERS URI was matched incorrectly.",
                testMatcher.match(TEST_TRAILERS_DIR), MoviesProvider.TRAILERS);
        assertEquals("Error: The REVIEWS URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEWS_DIR), MoviesProvider.REVIEWS);

        assertEquals("Error: The TRAILERS WITH MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_TRAILERS_WITH_MOVIES_DIR), MoviesProvider.TRAILERS_WITH_MOVIES);
        assertEquals("Error: The REVIEWS WITH MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEWS_WITH_MOVIES_DIR), MoviesProvider.REVIEWS_WITH_MOVIES);

    }
}
