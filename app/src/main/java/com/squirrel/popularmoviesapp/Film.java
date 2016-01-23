package com.squirrel.popularmoviesapp;

import java.io.Serializable;

/**
 * Created by squirrel on 1/20/16.
 */
public class Film implements Serializable {
    private final Long serialVersion = 1L;
    private String mId;
    private String mTitle;
    private String mReleaseDate;
    private String mOverview;
    private String mVoteAverage;
    private String mHasVideo;
    private String mPosterPath;

    public Film(String id, String title, String releaseDate, String overview,
                String voteAverage, String hasVideo, String posterPath) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mHasVideo = hasVideo;
        mPosterPath = posterPath;
    }

    @Override
    public String toString() {
        return "Image{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                ", mHasVideo='" + mHasVideo + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                '}';
    }

    public Long getSerialVersion() {
        return serialVersion;
    }

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


}
