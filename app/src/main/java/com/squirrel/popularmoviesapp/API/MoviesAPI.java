package com.squirrel.popularmoviesapp.API;

import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by squirrel on 1/25/16.
 */
public interface MoviesAPI {
    @GET("discover/movie/")
    public Call<ResponseWrapper<Movie>> getMovies(@Query("sort_by") String sort_by,
                          @Query("page") String page_num,
                          @Query("api_key") String api_key);


}
