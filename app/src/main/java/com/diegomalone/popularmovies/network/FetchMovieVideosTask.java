package com.diegomalone.popularmovies.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.diegomalone.popularmovies.model.MovieVideo;

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
 * Created by Diego Malone on 27/12/17.
 */

public class FetchMovieVideosTask extends AsyncTask<String, Void, List<MovieVideo>> {

    private OnTaskCompleted<List<MovieVideo>> mOnTaskCompleted;

    public FetchMovieVideosTask(OnTaskCompleted<List<MovieVideo>> onTaskCompleted) {
        mOnTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<MovieVideo> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String apiJsonResponse = null;
        String movieId = params[0];
        String apiKey = params[1];

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(movieId)
                    .appendPath("videos")
                    .appendQueryParameter("api_key", apiKey);

            URL url = new URL(builder.build().toString());

            Log.i("FetchMovieVideosTask", url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            apiJsonResponse = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return getMovieVideoListFromJson(apiJsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MovieVideo> movieVideos) {
        super.onPostExecute(movieVideos);

        if (movieVideos == null) {
            mOnTaskCompleted.onTaskError();
        } else {
            mOnTaskCompleted.onTaskCompleted(movieVideos);
        }
    }

    private List<MovieVideo> getMovieVideoListFromJson(String movieVideosJsonString)
            throws JSONException {
        List<MovieVideo> movieVideoList = new ArrayList<>();

        final String RESULTS = "results";
        final String VIDEO_ID = "id";
        final String LANGUAGE_CODE = "iso_639_1";
        final String COUNTRY_CODE = "iso_3166_1";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String VIDEO_TYPE = "type";

        JSONObject moviesJson = new JSONObject(movieVideosJsonString);
        JSONArray videoListJson = moviesJson.getJSONArray(RESULTS);

        for (int i = 0; i < videoListJson.length(); i++) {
            JSONObject videoJson = videoListJson.getJSONObject(i);

            String id = videoJson.getString(VIDEO_ID);
            String languageCode = videoJson.getString(LANGUAGE_CODE);
            String countryCode = videoJson.getString(COUNTRY_CODE);
            String key = videoJson.getString(KEY);
            String name = videoJson.getString(NAME);
            String site = videoJson.getString(SITE);
            int size = videoJson.getInt(SIZE);
            String type = videoJson.getString(VIDEO_TYPE);

            MovieVideo movieVideo = new MovieVideo(id, languageCode, countryCode, key, name, site, type, size);
            movieVideoList.add(movieVideo);
        }

        return movieVideoList;
    }
}
