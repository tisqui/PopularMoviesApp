package com.squirrel.popularmoviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by squirrel on 1/25/16.
 */
public class Movie implements Serializable {
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w342";

    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("original_title")
    @Expose
    private String mTitle;

    @SerializedName("release_date")
    @Expose
    private String mReleaseDate;

    @SerializedName("overview")
    @Expose
    private String mOverview;

    @SerializedName("vote_average")
    @Expose
    private String mVoteAverage;

    @SerializedName("video")
    @Expose
    private String mHasVideo;

    @SerializedName("poster_path")
    @Expose
    private String mPosterPath;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getHasVideo() {
        return mHasVideo;
    }

    public void setHasVideo(String hasVideo) {
        mHasVideo = hasVideo;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getFullPosterUrl(){
        return BASE_IMAGE_URL + IMAGE_SIZE + mPosterPath;
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                ", mHasVideo='" + mHasVideo + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                '}';
    }
}
