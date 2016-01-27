package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squirrel.popularmoviesapp.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailActivity extends BaseActivity {

    @Bind(R.id.film_detail_title) TextView mMovieTitle;
    @Bind(R.id.film_detail_year) TextView mYearLabel;
    @Bind(R.id.film_detail_average_votes) TextView mVotesLabel;
    @Bind(R.id.film_detail_poster) ImageView mMoviePoster;
    @Bind(R.id.film_detail_overview) TextView mMovieDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        activateToolbarWithHomeEnabled();
        ButterKnife.bind(this);

        //get the film data from the Intent
        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(FILM_DETAILS_KEY);

        //set the film data to the views
        mMovieTitle.setText(movie.getTitle());
        mYearLabel.setText(movie.getReleaseDate());
        mVotesLabel.setText("Rating: " + movie.getVoteAverage()+"/10");

        //Load the film poster image in the ImageView
        Picasso.with(this).load(movie.getFullPosterUrl())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mMoviePoster);

        mMovieDescription.setText(movie.getOverview());

    }

}
