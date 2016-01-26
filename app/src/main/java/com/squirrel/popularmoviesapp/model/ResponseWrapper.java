package com.squirrel.popularmoviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by squirrel on 1/25/16.
 */
public class ResponseWrapper<T> {
    @SerializedName("results")
    @Expose
    private List<T> mResults;

    public List<T> getResults() {
        return mResults;
    }

    public void setResults(List<T> results) {
        mResults = results;
    }
}
