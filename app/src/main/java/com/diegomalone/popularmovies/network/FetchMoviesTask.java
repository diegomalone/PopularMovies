package com.diegomalone.popularmovies.network;

import android.net.Uri;
import android.os.AsyncTask;

import com.diegomalone.popularmovies.model.Movie;

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
 * Created by malone on 7/24/16.
 */

public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    @Override
    protected List<Movie> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String apiJsonResponse = null;
        String listType = params[0];
        String apiKey = params[1];

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("http://api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(listType)
                    .appendQueryParameter("api_key", apiKey);

            URL url = new URL(builder.build().toString());

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
            return getMovieListFromJson(apiJsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Movie> getMovieListFromJson(String moviesJsonString)
            throws JSONException {
        List<Movie> movieList = new ArrayList<>();

        final String LIST_MOVIES = "results";
        final String MOVIE_TITLE = "title";

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray movieListJson = moviesJson.getJSONArray(LIST_MOVIES);

        for (int i = 0; i < movieListJson.length(); i++ ) {
            JSONObject movieJson = movieListJson.getJSONObject(i);

            String title = movieJson.getString(MOVIE_TITLE);

            Movie movie = new Movie(title);
            movieList.add(movie);
        }

        return movieList;
    }

}
