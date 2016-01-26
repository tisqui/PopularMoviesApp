package com.squirrel.popularmoviesapp.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.ResponseWrapper;

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

    public void getMovies(String sortBy, String page, APICallback<List<Movie>> callback){
        Call<ResponseWrapper<Movie>> call = mMoviesAPI.getMovies(sortBy, page, API_KEY);
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
