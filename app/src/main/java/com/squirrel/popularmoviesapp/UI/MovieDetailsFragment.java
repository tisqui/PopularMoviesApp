package com.squirrel.popularmoviesapp.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squirrel.popularmoviesapp.BaseActivity;
import com.squirrel.popularmoviesapp.BuildConfig;
import com.squirrel.popularmoviesapp.MovieReviewsActivity;
import com.squirrel.popularmoviesapp.R;
import com.squirrel.popularmoviesapp.TrailersListAdapter;
import com.squirrel.popularmoviesapp.api.MoviesAPIService;
import com.squirrel.popularmoviesapp.data.DBService;
import com.squirrel.popularmoviesapp.model.Movie;
import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by squirrel on 2/8/16.
 */
public class MovieDetailsFragment extends Fragment {
    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        //menu in fragment
        setHasOptionsMenu(true);

        //get the film data from the Intent
        Intent intent = getActivity().getIntent();
        mMovie = (Movie) intent.getSerializableExtra(BaseActivity.FILM_DETAILS_KEY);

        //get the movie data from the arguments
        if(mMovie == null) {
            Bundle extras = getArguments();
            mMovie = (Movie) extras.getSerializable(BaseActivity.FILM_DETAILS_KEY);
        }

        if(mMovie != null) {
            //set the film data to the views
            mMovieTitle.setText(mMovie.getTitle());
            mYearLabel.setText(mMovie.getReleaseDate());
            mVotesLabel.setText("Rating: " + mMovie.getVoteAverage() + "/10");

            //check if the film is favorite
            mDBService = new DBService(getActivity().getApplicationContext());
            mIsFavorite = mDBService.isFavorite(mMovie.getId());
            if (mIsFavorite) {
                mAddToFavorites.setImageResource(R.drawable.ic_bookmark);
            } else {
                mAddToFavorites.setImageResource(R.drawable.ic_bookmark_outline_plus);
            }

            //Load the film poster image in the ImageView
            Picasso.with(getActivity()).load(mMovie.getFullPosterUrl())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(mMoviePoster);

            mMovieDescription.setText(mMovie.getOverview());

            //set the list of trailers
            mArrayListOfTrailers = new ArrayList<Trailer>();
            mTrailersListAdapter = new TrailersListAdapter(getActivity(), mArrayListOfTrailers);
            mListViewTrailers.setAdapter(mTrailersListAdapter);

            mReviewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MovieReviewsActivity.class);
                    intent.putExtra(BaseActivity.REVIEWS_KEY, mMovie);
                    startActivityForResult(intent, 1);
                }
            });

            mAddToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsFavorite) {
                        mDBService.deleteFromFavorites(mMovie.getId());
                        mIsFavorite = false;
                        mAddToFavorites.setImageResource(R.drawable.ic_bookmark_outline_plus);
                        Toast.makeText(getActivity().getApplicationContext(), "Movie deleted from favorites", Toast.LENGTH_LONG).show();
                    } else {
                        mDBService.saveToFavorites(mMovie, null, null);
                        mIsFavorite = true;
                        mAddToFavorites.setImageResource(R.drawable.ic_bookmark);
                        Toast.makeText(getActivity().getApplicationContext(), "Movie added to favorites", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(getContext(), "Film information was not properly passed to the fragment", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTrailersListAdapter != null) {
            MoviesAPIService apiService = new MoviesAPIService(BuildConfig.MY_API_KEY);
            apiService.getTrailers(mMovie.getId(), new MoviesAPIService.APICallback<List<Trailer>>() {
                @Override
                public void onSuccess(List<Trailer> result) {
                    mTrailersListAdapter.addAll(result);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(LOG_TAG, t.getLocalizedMessage());
                    Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mMovie = (Movie) data.getSerializableExtra(BaseActivity.FILM_DETAILS_KEY);
            }
        }
    }

    //play youtube video in the youtube app our in the webview if there is no player
    public void playTrailer(String videoKey) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
            intent.putExtra("force_fullscreen", true);
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.YOUTUBE_BASEURL) + videoKey));
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareTrailerURL();
            return true;
        }
        if(id == R.id.action_settings){
            //this is the activity's menu item
            return false;
        }
        return false;
    }

    private void shareTrailerURL(){
        //get trailer's link
        Trailer trailer = mTrailersListAdapter.getItem(0);
        if (trailer != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, trailer.getYoutubeVideoLink());
            startActivity(Intent.createChooser(shareIntent, "Share trailer link using"));
        }
        else {
            Toast.makeText(getContext(), "There is no trailers to share", Toast.LENGTH_LONG).show();
        }

    }
}
