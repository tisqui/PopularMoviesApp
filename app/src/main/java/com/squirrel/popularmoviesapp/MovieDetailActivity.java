package com.squirrel.popularmoviesapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squirrel.popularmoviesapp.API.MoviesAPIService;
import com.squirrel.popularmoviesapp.data.DBService;
import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailActivity extends BaseActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Bind(R.id.film_detail_title)
    TextView mMovieTitle;

    @Bind(R.id.film_detail_year)
    TextView mYearLabel;

    @Bind(R.id.film_detail_average_votes)
    TextView mVotesLabel;

    @Bind(R.id.film_detail_poster)
    ImageView mMoviePoster;

    @Bind(R.id.film_detail_overview)
    TextView mMovieDescription;

    @Bind(R.id.reviews_button)
    Button mReviewsButton;

    @Bind(R.id.listview_trailers)
    ListView mListViewTrailers;

    @Bind(R.id.add_to_favorites)
    ImageView mAddToFavorites;

    private ArrayList<Trailer> mArrayListOfTrailers;
    private TrailersListAdapter mTrailersListAdapter;
    private Movie mMovie;
    private Boolean mIsFavorite;
    private DBService mDBService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        activateToolbarWithHomeEnabled();
        ButterKnife.bind(this);

        //get the film data from the Intent
        Intent intent = getIntent();
        mMovie = (Movie) intent.getSerializableExtra(FILM_DETAILS_KEY);

        //set the film data to the views
        mMovieTitle.setText(mMovie .getTitle());
        mYearLabel.setText(mMovie.getReleaseDate());
        mVotesLabel.setText("Rating: " + mMovie .getVoteAverage() + "/10");

        //check if the film is favorite
        mDBService = new DBService(getApplicationContext());
        checkIfFavorite();
        if(mIsFavorite){
            mAddToFavorites.setImageResource(R.drawable.ic_bookmark);
        }else{
            mAddToFavorites.setImageResource(R.drawable.ic_bookmark_outline_plus);
        }

        //Load the film poster image in the ImageView
        Picasso.with(this).load(mMovie .getFullPosterUrl())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mMoviePoster);

        mMovieDescription.setText(mMovie .getOverview());

        //set the list of trailers
        mArrayListOfTrailers = new ArrayList<Trailer>();
        mTrailersListAdapter = new TrailersListAdapter(this, mArrayListOfTrailers);
        mListViewTrailers.setAdapter(mTrailersListAdapter);

        mReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this, MovieReviewsActivity.class);
                intent.putExtra(REVIEWS_KEY, mMovie);
                startActivityForResult(intent, 1);
            }
        });

        mAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsFavorite){
                    mDBService.deleteFromFavorites(mMovie.getId());
                    mIsFavorite = false;
                    mAddToFavorites.setImageResource(R.drawable.ic_bookmark_outline_plus);
                    Toast.makeText(getApplicationContext(), "Movie deleted from favorites", Toast.LENGTH_LONG).show();
                }else{
                    mDBService.saveToFavorites(mMovie, null, null);
                    mIsFavorite = true;
                    mAddToFavorites.setImageResource(R.drawable.ic_bookmark);
                    Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                mMovie = (Movie) data.getSerializableExtra(FILM_DETAILS_KEY);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mTrailersListAdapter != null){
            MoviesAPIService apiService = new MoviesAPIService(BuildConfig.MY_API_KEY);
            apiService.getTrailers(mMovie.getId(), new MoviesAPIService.APICallback<List<Trailer>>() {
                @Override
                public void onSuccess(List<Trailer> result) {
                    mTrailersListAdapter.addAll(result);

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(LOG_TAG, t.getLocalizedMessage());
                    Toast.makeText(MovieDetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
            //set the click listener for the list
            mListViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    playTrailer(mTrailersListAdapter.getTrailerByPosition(position).getKey());
                }
            });
        }

    }


    //play youtube video in the youtube app our in the webview if there is no player
    public void playTrailer(String videoKey) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
            intent.putExtra("force_fullscreen",true);
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.YOUTUBE_BASEURL)+videoKey));
            startActivity(intent);
        }
    }

    private void checkIfFavorite(){
        List<Movie> favoriteMovies = mDBService.getFavoriteMovies();
        mIsFavorite = false;
        for(Movie item : favoriteMovies){
            if(item.getId().equals(mMovie.getId())){
                mIsFavorite = true;
            }
        }
    }

}
