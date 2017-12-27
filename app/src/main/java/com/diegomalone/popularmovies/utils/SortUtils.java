package com.diegomalone.popularmovies.utils;

import android.content.SharedPreferences;

/**
 * Created by Diego Malone on 27/12/17.
 */

public class SortUtils {

    public static final String PREF_SORT_KEY = "sortKey";
    public static final String PREF_SORT_DEFAULT_VALUE = "popular";

    public static final String SORT_MOST_POPULAR = "popular";
    public static final String SORT_HIGHEST_RATING = "top_rated";

    private SharedPreferences sharedPreferences;

    public SortUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getSortPreference() {
        return sharedPreferences.getString(PREF_SORT_KEY, PREF_SORT_DEFAULT_VALUE);
    }

    public void saveSortPreference(String preference) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREF_SORT_KEY, preference);
        editor.apply();
    }

    public int getSortPreferenceOrdinal() {
        return getSortPreference().equals(SORT_MOST_POPULAR) ? 0 : 1;
    }

    public String getPreferenceFromOrdinal(int ordinal) {
        return ordinal == 0 ? SORT_MOST_POPULAR : SORT_HIGHEST_RATING;
    }
}
