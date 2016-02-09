package com.squirrel.popularmoviesapp.data;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Review;
import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 2/5/16.
 */
public class TestDBService extends AndroidTestCase {

    private static final String LOG_TAG = TestDBService.class.getSimpleName();
    private DBService mDBService;

    public void deleteAllRecords() {

        deleteAllRecordsFromContentProvider();
    }

    public void deleteAllRecordsFromContentProvider() {

        mDBService.deleteAllMoviesFromFavorites();

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

    // Strart each test to start with a clean slate, run deleteAllRecords
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDBService = new DBService(getContext());
        deleteAllRecords();
    }

    public void testSaveToFavorites(){
        Movie movie = TestUtil.createMovie("1");
        List<Trailer> trailers = new ArrayList<Trailer>();
        List<Review> reviews = new ArrayList<Review>();
        for(int i = 0; i<10; i++){
            Trailer trailer = TestUtil.createTrailer(String.valueOf(i));
            trailers.add(trailer);
            Review review = TestUtil.createReview(String.valueOf(i));
            reviews.add(review);
        }
        mDBService.saveToFavorites(movie, trailers, reviews);
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Movie is not saved to the Movie table", 1, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MoviesContract.TrailersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Trailers are not saved to the Trailers table", 10, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Reviews are not saved to the reviews table", 10, cursor.getCount());
        cursor.close();
    }

    public void testGetFavoriteMoviesFromDB(){
        List<Movie> result;
        List<Movie> initialList = new ArrayList<Movie>();
        deleteAllRecordsFromContentProvider();

        for(int i = 0; i<10; i++){
            Movie movie = TestUtil.createMovie(String.valueOf(i));
            mDBService.saveToFavorites(movie, null, null);
            initialList.add(movie);
        }

        result = mDBService.getFavoriteMovies();
        assertEquals("Error: Not all the Movies are returned from DB", initialList.size(), result.size());
        Boolean equals = true;
            for(int i=0; i< result.size(); i++){
                equals = TestUtil.compareMovies(initialList.get(i), result.get(i));
            }
        assertTrue("Error: Movies returned from DB do not equal the actual movies", equals);
    }

    public void testGetFavoriteTrailersFromDB(){
        List<Trailer> result;
        List<Trailer> initialList = new ArrayList<Trailer>();
        deleteAllRecordsFromContentProvider();
        Movie movie = TestUtil.createMovie("1");

        for(int i = 0; i<10; i++){
            Trailer trailer = TestUtil.createTrailer(String.valueOf(i));
            initialList.add(trailer);
        }
        mDBService.saveToFavorites(movie,  initialList, null);


        result = mDBService.getFavoriteMovieTrailers("1");
        assertEquals("Error: Not all the Trailers are returned from DB", initialList.size(), result.size());
        Boolean equals = true;
        for(int i=0; i< result.size(); i++){
            equals = TestUtil.compareTrailers(initialList.get(i), result.get(i));
        }
        assertTrue("Error: Trailers returned from DB do not equal the actual movies", equals);
    }

    public void testGetFavoriteReviewsFromDB(){
        List<Review> result;
        List<Review> initialList = new ArrayList<Review>();
        deleteAllRecordsFromContentProvider();
        Movie movie = TestUtil.createMovie("1");

        for(int i = 0; i<10; i++){
            Review review= TestUtil.createReview(String.valueOf(i));
            initialList.add(review);
        }
        mDBService.saveToFavorites(movie,  null, initialList);


        result = mDBService.getFavoriteMovieReviews("1");
        assertEquals("Error: Not all the Reviews are returned from DB", initialList.size(), result.size());
        Boolean equals = true;
        for(int i=0; i< result.size(); i++){
            equals = TestUtil.compareReviews(initialList.get(i), result.get(i));
        }
        assertTrue("Error: Reviews returned from DB do not equal the actual reviews", equals);
    }

    public void testDeletingFromFavorites(){
        deleteAllRecordsFromContentProvider();
        List<Movie> result;
        List<Movie> initialList = new ArrayList<Movie>();
        deleteAllRecordsFromContentProvider();

        for(int i = 0; i<10; i++){
            Movie movie = TestUtil.createMovie(String.valueOf(i));
            mDBService.saveToFavorites(movie, null, null);
            initialList.add(movie);
        }
        mDBService.deleteFromFavorites("0");
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Movie is not deleted", 9, cursor.getCount());
        cursor.close();

        for(int i = 1; i<10; i++){
            mDBService.deleteFromFavorites(String.valueOf(i));
        }
        cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Movies are not deleted", 0, cursor.getCount());
        cursor.close();

    }
}
