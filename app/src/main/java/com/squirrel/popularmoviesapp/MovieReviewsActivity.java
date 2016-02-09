package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.squirrel.popularmoviesapp.api.MoviesAPIService;
import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieReviewsActivity extends BaseActivity {

    private static final String LOG_TAG = MovieReviewsActivity.class.getSimpleName();
    private ArrayList<Review> mArrayListOfReviews;
    private ReviewsListAdapter mReviewsListAdapter;
    private Movie mMovie;

    @Bind(R.id.listview_reviews)
    ListView mListViewReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        activateToolbarWithHomeEnabled();
        ButterKnife.bind(this);

        //get the film data from the Intent
        Intent intent = getIntent();
        mMovie = (Movie) intent.getSerializableExtra(REVIEWS_KEY);

        //set the list of trailers
        mArrayListOfReviews = new ArrayList<Review>();
        mReviewsListAdapter = new ReviewsListAdapter(this, mArrayListOfReviews);
        mListViewReviews.setAdapter(mReviewsListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mReviewsListAdapter != null){
            MoviesAPIService apiService = new MoviesAPIService(BuildConfig.MY_API_KEY);
            apiService.getReviews(mMovie.getId(), "1", new MoviesAPIService.APICallback<List<Review>>() {
                @Override
                public void onSuccess(List<Review> result) {
                    mReviewsListAdapter.addAll(result);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(LOG_TAG, t.getLocalizedMessage());
                    Toast.makeText(MovieReviewsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(FILM_DETAILS_KEY, mMovie);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(FILM_DETAILS_KEY, mMovie);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return true;
    }

}
