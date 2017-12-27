package com.diegomalone.popularmovies.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.diegomalone.popularmovies.model.MovieReview;

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

public class FetchMovieReviewsTask extends AsyncTask<String, Void, List<MovieReview>> {

    private OnTaskCompleted<List<MovieReview>> mOnTaskCompleted;

    public FetchMovieReviewsTask(OnTaskCompleted<List<MovieReview>> onTaskCompleted) {
        mOnTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<MovieReview> doInBackground(String... params) {

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
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", apiKey);

            URL url = new URL(builder.build().toString());

            Log.i("FetchMovieReviewsTask", url.toString());

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
            return getMovieReviewListFromJson(apiJsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MovieReview> movieReviews) {
        super.onPostExecute(movieReviews);

        if (movieReviews == null) {
            mOnTaskCompleted.onTaskError();
        } else {
            mOnTaskCompleted.onTaskCompleted(movieReviews);
        }
    }

    private List<MovieReview> getMovieReviewListFromJson(String movieReviewsJsonString)
            throws JSONException {
        List<MovieReview> movieReviewList = new ArrayList<>();

        final String RESULTS = "results";
        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        JSONObject moviesJson = new JSONObject(movieReviewsJsonString);
        JSONArray reviewListJson = moviesJson.getJSONArray(RESULTS);

        for (int i = 0; i < reviewListJson.length(); i++) {
            JSONObject reviewJson = reviewListJson.getJSONObject(i);

            String id = reviewJson.getString(REVIEW_ID);
            String author = reviewJson.getString(AUTHOR);
            String content = reviewJson.getString(CONTENT);
            String url = reviewJson.getString(URL);

            MovieReview movieReview = new MovieReview(author, url, content, id);
            movieReviewList.add(movieReview);
        }

        return movieReviewList;
    }
}
