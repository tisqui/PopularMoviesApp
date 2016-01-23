package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FilmDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        activateToolbarWithHomeEnabled();

        //get the film data from the Intent
        Intent intent = getIntent();
        Film film = (Film) intent.getSerializableExtra(FILM_DETAILS_KEY);

        //set the film data to the views
        TextView filmTitle = (TextView) findViewById(R.id.film_detail_title);
        filmTitle.setText(film.getTitle());

        TextView yearLabel = (TextView) findViewById(R.id.film_detail_year);
        yearLabel.setText(film.getReleaseDate());

        TextView votesLabel = (TextView) findViewById(R.id.film_detail_average_votes);
        votesLabel.setText("Rating: " + film.getVoteAverage()+"/10");

        //Load the film poster image in the ImageView
        ImageView filmPoster = (ImageView) findViewById(R.id.film_detail_poster);
        Picasso.with(this).load(film.getPosterPath())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(filmPoster);

        TextView filmDescription = (TextView) findViewById(R.id.film_detail_overview);
        filmDescription.setText(film.getOverview());

    }

}
