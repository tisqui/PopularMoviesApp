package com.squirrel.popularmoviesapp.API;

import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.ResponseWrapper;
import com.squirrel.popularmoviesapp.model.Review;
import com.squirrel.popularmoviesapp.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by squirrel on 1/25/16.
 */
public interface MoviesAPI {
    @GET("discover/movie/")
    public Call<ResponseWrapper<Movie>> getMovies(@Query("sort_by") String sort_by,
                          @Query("page") String pageNum,
                          @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    public Call<ResponseWrapper<Trailer>> getTrailers(@Path("movie_id") String movieId,
                                                      @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    public Call<ResponseWrapper<Review>> getReviews(@Path("movie_id") String movieId,
                                                    @Query("page") String pageNum,
                                                    @Query("api_key") String apiKey);
}
