package com.squirrel.popularmoviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by squirrel on 1/27/16.
 */
public class Review implements Serializable {
    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("author")
    @Expose
    private String mAuthor;

    @SerializedName("content")
    @Expose
    private String mContent;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mId='" + mId + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }
}
