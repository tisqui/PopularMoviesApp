package com.squirrel.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by squirrel on 1/20/16.
 */
public class ImageGridAdapter extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater inflater;

    private String[] imageUrls;

    public ImageGridAdapter(Context context, String[] imageUrls) {

        super(context, R.layout.gridview_item, imageUrls);

        this.mContext = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void add(Object object) {
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }

        Picasso.with(mContext)
                .load(imageUrls[position])
                .fit()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into((ImageView) convertView);

        return convertView;
    }


}
