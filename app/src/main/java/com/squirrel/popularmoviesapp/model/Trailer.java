package com.squirrel.popularmoviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by squirrel on 1/27/16.
 */
public class Trailer implements Serializable {
    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("key")
    @Expose
    private String mKey;

    @SerializedName("name")
    @Expose
    private String mName;
}
