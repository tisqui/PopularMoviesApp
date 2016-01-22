package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<Image> mImagesList = new ArrayList<Image>();
    private RecyclerView mRecyclerView;
    private RecyclerGridViewAdapter mRecyclerGridViewAdapter;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToolbar();

        mSortOrder = getString(R.string.order_setting_default_value);

        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerGridViewAdapter = new RecyclerGridViewAdapter(MainActivity.this, new ArrayList<Image>());
        mRecyclerView.setAdapter(mRecyclerGridViewAdapter);

        mRecyclerView.addOnItemTouchListener(new FilmClickListener(this, mRecyclerView,
                new FilmClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, FilmDetailActivity.class);
                        intent.putExtra(FILM_DETAILS_KEY, mRecyclerGridViewAdapter.getImage(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, FilmDetailActivity.class);
                        intent.putExtra(FILM_DETAILS_KEY, mRecyclerGridViewAdapter.getImage(position));
                        startActivity(intent);
                    }
                }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerGridViewAdapter != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            mSortOrder = sharedPref.getString(getString(R.string.order_key_text), getString(R.string.order_setting_default_value));

            GetJson getJson = new GetJson(mSortOrder) {
                @Override
                protected void afterCompletionWhenDataDownloaded() {
                    mRecyclerGridViewAdapter.updateImagesInGrid(getImagesList());
                }
            };
            getJson.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
