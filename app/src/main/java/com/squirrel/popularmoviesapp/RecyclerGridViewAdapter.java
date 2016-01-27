package com.squirrel.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.squirrel.popularmoviesapp.model.Movie;

import java.util.List;

/**
 * Created by squirrel on 1/20/16.
 */
public class RecyclerGridViewAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<Movie> mImagesList;
    private Context mContext;
    private final String LOG_TAG = RecyclerGridViewAdapter.class.getSimpleName();

    public RecyclerGridViewAdapter(Context context, List<Movie> mImagesList) {
        mContext = context;
        this.mImagesList = mImagesList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Movie movieItem = mImagesList.get(position);
        Log.d(LOG_TAG, "Processing the item: " + movieItem.getTitle() + " " + position);
        Picasso.with(mContext).load(movieItem.getFullPosterUrl())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnailImage);
    }

    @Override
    public int getItemCount() {
        return (null != mImagesList ? mImagesList.size() : 0);
    }

    public void updateImagesInGrid(List<Movie> newFilms){
        mImagesList = newFilms;
        notifyDataSetChanged();
    }


    public Movie getImage(int position){
        if(mImagesList != null){
            return mImagesList.get(position);
        }
        return null;
    }
}
