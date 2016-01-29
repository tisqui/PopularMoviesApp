package com.squirrel.popularmoviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squirrel.popularmoviesapp.R;

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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getYoutubeVideoLink() {
        return (R.string.YOUTUBE_BASE_URL) + mKey;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "mId='" + mId + '\'' +
                ", mKey='" + mKey + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }
}
