package com.squirrel.popularmoviesapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by squirrel on 1/31/16.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.squirrel.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";

    interface MoviesColumns{
        String MOVIE_ID = "id";
        String MOVIE_TITLE = "title";
        String MOVIE_RELEASE_DATE = "release_date";
        String MOVIE_OVERVIEW = "overview";
        String MOVIE_VOTE_AVERAGE = "vote_average";
        String MOVIE_VIDEO = "has_video";
        String MOVIE_POSTER_PATH = "poster_path";
    }

    interface TrailersColumns{
        String TRAILER_ID = "id";
        String MOVIE_KEY = "movie_id";
        String TRAILER_KEY = "key";
        String TRAILER_NAME = "name";
    }

    interface ReviewsColumns{
        String REVIEW_ID = "id";
        String MOVIE_KEY = "movie_id";
        String REVIEW_AUTHOR = "author";
        String REVIEW_CONTENT = "content";
    }


    public static class MoviesEntry implements MoviesColumns, BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


        public static Uri buildMovieUri(String movieId){
            return CONTENT_URI.buildUpon().appendEncodedPath(movieId).build();
        }

        public static String getMovieId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

    public static class TrailersEntry implements TrailersColumns, BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static Uri buildTrailerUri(String trailerId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(trailerId).build();

        }

        public static Uri buildTrailerMovie(String movieSettings) {

            return CONTENT_URI.buildUpon().appendPath(movieSettings).build();

        }
    }

    public static class ReviewEntry implements ReviewsColumns, BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static Uri buildReviewUri(String trailerId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(trailerId).build();

        }

        public static Uri buildReviewMovie(String movieSettings) {

            return CONTENT_URI.buildUpon().appendPath(movieSettings).build();

        }
    }


}
