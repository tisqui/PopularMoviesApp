package com.squirrel.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by squirrel on 1/31/16.
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();
    void deleteTheDatabase() {
        mContext.deleteDatabase(MoviesDB.DATABASE_NAME);
    }
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreatingDB()throws Throwable{

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MoviesDB.Tables.MOVIES);
        tableNameHashSet.add(MoviesDB.Tables.REVIEWS);
        tableNameHashSet.add(MoviesDB.Tables.REVIEWS);

        mContext.deleteDatabase(MoviesDB.DATABASE_NAME);

        //create the DB
        SQLiteDatabase db = new MoviesDB(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // check if the tables were created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: DB creation error", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without all 3 tables that expected",
                tableNameHashSet.isEmpty());

        // checking columns
        c = db.rawQuery("PRAGMA table_info(" + MoviesDB.Tables.MOVIES + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> moviesColumnHashSet = new HashSet<String>();
        moviesColumnHashSet.add(MoviesContract.MoviesEntry._ID);
        moviesColumnHashSet.add(MoviesContract.MoviesEntry.MOVIE_TITLE);
        moviesColumnHashSet.add(MoviesContract.MoviesEntry.MOVIE_OVERVIEW);
        moviesColumnHashSet.add(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH);
        moviesColumnHashSet.add(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE);
        moviesColumnHashSet.add(MoviesContract.MoviesEntry.MOVIE_VIDEO);
        moviesColumnHashSet.add(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            moviesColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required movies entry columns",
                moviesColumnHashSet.isEmpty());
        db.close();
    }

    public void testMoviesTable(){
        //get writable DB
        MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();

        ContentValues movieValues = TestUtil.createMovieTableValues();

        long movie_id;
        movie_id = db.insert(MoviesDB.Tables.MOVIES, null, movieValues);

        assertTrue(movie_id != -1);
        Cursor cursor = db.query(MoviesDB.Tables.MOVIES,
                null, null, null, null, null, null);
        assertTrue("No records returned, but should to", cursor.moveToFirst());

        TestUtil.validateCurrentRecord("Quesry is not validated", cursor, movieValues);
        cursor.close();
    }

    public void testTrailersTable(){
        long movieId = insertMovie();
        assertFalse("Error: Location Not Inserted Correctly", movieId == -1L);

       MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();
        ContentValues trailerTableValues = TestUtil.createTrailerTableValues(movieId);

        long trailerRowId = db.insert(MoviesDB.Tables.TRAILERS, null, trailerTableValues);
        assertTrue(trailerRowId != -1);

        Cursor weatherCursor = db.query(MoviesDB.Tables.TRAILERS,null, null, null, null, null, null);

        assertTrue( "Error: No Records returned from trailer query", weatherCursor.moveToFirst() );
        TestUtil.validateCurrentRecord("testInsertReadDb moviesEntry failed to validate",
                weatherCursor, trailerTableValues);

        assertFalse("Error: More than one record returned from movies query",
                weatherCursor.moveToNext());

        weatherCursor.close();
        moviesDB.close();
    }

    public void testReviewsTable(){
        long movieId = insertMovie();
        assertFalse("Error: Location Not Inserted Correctly", movieId == -1L);

        MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();
        ContentValues reviewsTableValues = TestUtil.createReviewTableValues(movieId);

        long reviewRowId = db.insert(MoviesDB.Tables.REVIEWS, null, reviewsTableValues);
        assertTrue(reviewRowId != -1);

        Cursor weatherCursor = db.query(MoviesDB.Tables.REVIEWS,null, null, null, null, null, null);

        assertTrue( "Error: No Records returned from trailer query", weatherCursor.moveToFirst() );
        TestUtil.validateCurrentRecord("testInsertReadDb moviesEntry failed to validate",
                weatherCursor, reviewsTableValues);

        assertFalse("Error: More than one record returned from movies query",
                weatherCursor.moveToNext());

        weatherCursor.close();
        moviesDB.close();
    }

    public long insertMovie(){
        MoviesDB moviesDB = new MoviesDB(mContext);
        SQLiteDatabase db = moviesDB.getWritableDatabase();
        ContentValues testValues = TestUtil.createMovieTableValues();

        long movieId;
        movieId = db.insert(MoviesDB.Tables.MOVIES, null, testValues);
        assertTrue(movieId != -1);

        Cursor cursor = db.query(MoviesDB.Tables.MOVIES,null, null, null, null, null, null);
        assertTrue( "Error: No Records returned from movies query", cursor.moveToFirst() );
        TestUtil.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);
        assertFalse( "Error: More than one record returned from movies query",
                cursor.moveToNext() );
        cursor.close();
        db.close();
        return movieId;
    }

}
