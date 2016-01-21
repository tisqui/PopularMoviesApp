package com.squirrel.popularmoviesapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 1/20/16.
 */
public class GetJson {
    private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private final String IMAGE_SIZE = "w185";
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    private final String SORT_TAG = "sort_by";
    private final String API_KEY_TAG = "api_key";
    private final String API_KEY = "[INCERT_KEY_HERE]";

    private String LOG_TAG = GetJson.class.getSimpleName();
    private List<Image> mImagesList;
    private Uri mUri;

    private String mStringUrl;
    private String mJsonData;

    public GetJson(String sortType){
        getUrl(sortType);
        mStringUrl = mUri.toString();
        mImagesList = new ArrayList<Image>();
    }

    public List<Image> getImagesList() {
        return mImagesList;
    }

    private void getUrl(String sortType){
        mUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_TAG, sortType)
                .appendQueryParameter(API_KEY_TAG,API_KEY)
                .build();
    }

    public void processJson(){
        DownloadJson downloadJson = new DownloadJson();
        downloadJson.execute(mStringUrl);
    }

    public void parseJson(){
        final String RESULTS_OBJ = "results";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String ID = "id";
        final String TITLE= "title";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String HAS_VIDEO = "video";


        try {
            JSONObject input = new JSONObject(mJsonData);
            JSONArray array = input.getJSONArray(RESULTS_OBJ);

            for(int i=0; i<array.length(); i++){
                JSONObject film = array.getJSONObject(i);

                //get id, title, release date, overview, vote average, hasvideo, poster path
                String id = film.getString(ID);
                String title = film.getString(TITLE);
                String releaseDate = film.getString(RELEASE_DATE);
                String overview = film.getString(OVERVIEW);
                String voteAverage = film.getString(VOTE_AVERAGE);
                String hasVideo = film.getString(HAS_VIDEO);
                String path = film.getString(POSTER_PATH);

                //need to add base image url and image size to get complete image url

                String posterLink = BASE_IMAGE_URL + "w185" + path;

                //String id, String title, String releaseDate, String overview,String voteAverage, String hasVideo, String posterPath
                Image image = new Image(id, title, releaseDate, overview, voteAverage, hasVideo, posterLink);

                this.mImagesList.add(image);
            }

            for(Image image : mImagesList){
                Log.i(LOG_TAG, image.toString());
            }

        }catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "Something went wrong! Error procesing JSON.");
        }
    }

    public class DownloadJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpConnection = null;
            BufferedReader bufferedReader = null;

            if(params==null)
                return null;

            try {
                URL url = new URL(params[0]);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                InputStream inputStream = httpConnection.getInputStream();
                if(inputStream==null){
                    return null;
                }
                StringBuffer buff = new StringBuffer();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String lineOfText;

                while((lineOfText = bufferedReader.readLine()) != null){
                    buff.append(lineOfText+"\n");
                }

                return buff.toString();

            }catch(IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;

            }finally{
                if(httpConnection != null){
                    httpConnection.disconnect();
                }
                if(bufferedReader != null){
                    try{
                        bufferedReader.close();

                    }catch (IOException e){
                        Log.e(LOG_TAG, "Error closing bufferedReader", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mJsonData = s;
            Log.v(LOG_TAG, "Data got from the stream: " + mJsonData);
            if(mJsonData == null) {
               Log.d(LOG_TAG, "Empty data downloaded");
            } else {
                parseJson();
            }
        }
    }

}
