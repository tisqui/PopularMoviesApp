package com.squirrel.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squirrel.popularmoviesapp.model.Review;

import java.util.ArrayList;

/**
 * Created by squirrel on 1/28/16.
 */
public class ReviewsListAdapter extends ArrayAdapter<Review> {
    private ArrayList<Review> mReviewArrayList;

    public ReviewsListAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
        mReviewArrayList = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Review review = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_list_item, parent, false);
        }
        TextView author = (TextView) convertView.findViewById(R.id.review_author);
        TextView reviewText = (TextView) convertView.findViewById(R.id.review_text);
        author.setText(review.getAuthor());
        reviewText.setText(review.getContent());

        // Return the completed view to render on screen
        return convertView;
    }

}
