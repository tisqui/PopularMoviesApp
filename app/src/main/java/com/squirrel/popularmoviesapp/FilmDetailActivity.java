package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FilmDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        Image film = (Image) intent.getSerializableExtra(FILM_DETAILS_KEY);

        TextView textView = (TextView) findViewById(R.id.detail_textview);
        textView.setText(film.toString());

    }

}
