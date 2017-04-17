package com.orie.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.orie.popularmovies.R;

/**
 * Created by Orie Victor on 4/16/2017.
 */

public class MoviePreferences {
    /*
   * The order in which to sort the movie
   * */
        public static boolean isRatedHigh(Context context) {
        //Return true if the user's preference for Sort order is High Rated,
        // false otherwise
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForSort = context.getString(R.string.pref_sort_key);
        String defaultSort = context.getString(R.string.pref_sort_rating);
        String preferredSort = prefs.getString(keyForSort,defaultSort);
        String highRated = context.getString(R.string.pref_sort_rating);
        boolean userPrefersHighRated;
        if(highRated.equals(preferredSort)){
            userPrefersHighRated = true;
        }else {
            userPrefersHighRated = false;
        }

        return userPrefersHighRated;

    }
}
