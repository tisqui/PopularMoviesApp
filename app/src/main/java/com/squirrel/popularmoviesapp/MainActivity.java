package com.squirrel.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
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

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.application_toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        getToolbar();
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return mToolbar;
    }

}
