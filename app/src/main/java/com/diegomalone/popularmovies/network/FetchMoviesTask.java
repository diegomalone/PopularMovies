package com.diegomalone.popularmovies.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.diegomalone.popularmovies.model.Movie;

import org.apache.commons.lang3.StringUtils;
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

    private static String TAG = FetchMoviesTask.class.getSimpleName();

    private OnTaskCompleted<List<Movie>> mOnTaskCompleted;

    public FetchMoviesTask(OnTaskCompleted<List<Movie>> onTaskCompleted) {
        mOnTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String apiJsonResponse = null;
        String sortBy = params[0];
        String page = params[1];
        String apiKey = params[2];

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(sortBy)
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("page", page);

            URL url = new URL(builder.build().toString());

            Log.i(TAG, "Fetch movies url: " + url.toString());

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

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);

        if (movies == null) {
            mOnTaskCompleted.onTaskError();
        } else {
            mOnTaskCompleted.onTaskCompleted(movies);
        }
    }

    private List<Movie> getMovieListFromJson(String moviesJsonString)
            throws JSONException {
        List<Movie> movieList = new ArrayList<>();

        final String LIST_MOVIES = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_BACKGROUND_PHOTO = "backdrop_path";
        final String MOVIE_SYNOPSIS = "overview";
        final String MOVIE_USER_RATING = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray movieListJson = moviesJson.getJSONArray(LIST_MOVIES);

        for (int i = 0; i < movieListJson.length(); i++) {
            JSONObject movieJson = movieListJson.getJSONObject(i);

            long id = movieJson.getLong(MOVIE_ID);
            String title = movieJson.getString(MOVIE_TITLE);
            String posterPath = movieJson.getString(MOVIE_POSTER);
            String backgroundPhotoPath = movieJson.getString(MOVIE_BACKGROUND_PHOTO);
            String synopsis = movieJson.getString(MOVIE_SYNOPSIS);
            String releaseDate = movieJson.getString(MOVIE_RELEASE_DATE);
            double userRating = movieJson.getDouble(MOVIE_USER_RATING);

            // Remove the backslash in the paths
            posterPath = StringUtils.substring(posterPath, 1);
            backgroundPhotoPath = StringUtils.substring(backgroundPhotoPath, 1);

            Movie movie = new Movie(id, title, synopsis, releaseDate, posterPath, backgroundPhotoPath, userRating);
            movieList.add(movie);
        }

        return movieList;
    }

}
