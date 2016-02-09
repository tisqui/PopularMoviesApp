package com.squirrel.popularmoviesapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.ResponseWrapper;
import com.squirrel.popularmoviesapp.model.Review;
import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by squirrel on 1/25/16.
 */
public class MoviesAPIService {
    private static final String LOG_TAG = MoviesAPIService.class.getSimpleName();
    public static final String BASE_API_URL = "http://api.themoviedb.org/3/";
    private final String API_KEY;

    private Gson mGson;
    private Retrofit mRetrofit;
    private MoviesAPI mMoviesAPI;

    public interface APICallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }

    public MoviesAPIService(String apiKey) {
        API_KEY = apiKey;
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();

        mMoviesAPI = mRetrofit.create(MoviesAPI.class);
    }

    /**
     * Get movies list from API
     * @param sortBy sorting order parameter
     * @param page number of the page we are requesting
     * @param callback call back to get the list of the movies from the object that wraps it
     */
    public void getMovies(String sortBy, String page, APICallback<List<Movie>> callback){
        Call<ResponseWrapper<Movie>> call = mMoviesAPI.getMovies(sortBy, page, API_KEY);
        get(call, callback);
    }

    /**
     * Get trailers from the API
     * @param movieId the id of the movie we are requesting trailers from
     * @param callback call back to get the list of the trailers from the object that wraps it
     */
    public void getTrailers(String movieId, APICallback<List<Trailer>> callback){
        Call<ResponseWrapper<Trailer>> call = mMoviesAPI.getTrailers(movieId, API_KEY);
        get(call, callback);
    }

    /**
     * Get reviews from API
     * @param movieId
     * @param page
     * @param callback call back to get the list of the reviews from the object that wraps it
     */
    public void getReviews(String movieId, String page, APICallback<List<Review>> callback){
        Call<ResponseWrapper<Review>> call = mMoviesAPI.getReviews(movieId, page, API_KEY);
        get(call, callback);
    }


    private <T> void get(Call<ResponseWrapper<T>> call, final APICallback<List<T>> callback){

        call.enqueue(new Callback<ResponseWrapper<T>>() {
            @Override
            public void onResponse(Response<ResponseWrapper<T>> response) {
                int statusCode = response.code();
               try {
                   callback.onSuccess(response.body().getResults());
               } catch (Throwable t){
                   callback.onFailure(t);
               }

            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
