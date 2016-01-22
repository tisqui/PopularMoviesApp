package com.squirrel.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by squirrel on 1/20/16.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    protected ImageView thumbnailImage;

    public ImageViewHolder(View itemView) {
        super(itemView);
        this.thumbnailImage = (ImageView) itemView.findViewById(R.id.image_holder);
    }

}
