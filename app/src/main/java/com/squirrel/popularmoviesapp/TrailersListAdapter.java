package com.squirrel.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squirrel.popularmoviesapp.model.Trailer;

import java.util.ArrayList;

/**
 * Created by squirrel on 1/28/16.
 */
public class TrailersListAdapter extends ArrayAdapter<Trailer> {

    private ArrayList<Trailer> mTrailersList;

    public TrailersListAdapter(Context context, ArrayList<Trailer> trailers) {
        super(context, 0, trailers);
        mTrailersList = trailers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Trailer trailer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailers_list_item, parent, false);
        }
        // Lookup view for data population
        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_title);
        // Populate the data into the template view using the data object
        trailerName.setText(trailer.getName());
        // Return the completed view to render on screen

        return convertView;
    }

    public Trailer getTrailerByPosition(int position){
        if(mTrailersList != null){
            return mTrailersList.get(position);
        }
        return null;
    }
}
