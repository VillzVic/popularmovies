package com.orie.popularmovies.utilities;

import android.content.Context;
import android.graphics.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.id;

/**
 * Created by Orie Victor on 4/16/2017.
 */

public class MovieJsonUtils {


//class to retrieve the data
    public static String[] getMovieFromJson (Context context, String movieJsonString)
            throws JSONException {

        final String RESULTS = "results";
        final String TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE_RATING = "vote_average";
        final String ID = "id";
        final String BACKDROP_PATH = "backdrop_path";

        /* String array to hold each movie detail */
        String[] MovieParsedData = null;
        JSONObject movieJsonObject = new JSONObject(movieJsonString);

        JSONArray movieArray = movieJsonObject.getJSONArray(RESULTS);

        MovieParsedData = new String[movieArray.length()];


        for (int i = 0; i < movieArray.length(); i++){

            String title;
            String poster_path;
            String date_released;
            String overview;
            String vote_average_rating;
            String id;
            String  backdrop_path;


            JSONObject eachMovie = movieArray.getJSONObject(i);
            title = eachMovie.getString(TITLE);
            poster_path = eachMovie.getString(POSTER_PATH);
            date_released = eachMovie.getString(RELEASE_DATE);
            overview = eachMovie.getString(OVERVIEW);
            backdrop_path = eachMovie.getString(BACKDROP_PATH);
            vote_average_rating = eachMovie.getString(VOTE_AVERAGE_RATING);
            id = eachMovie.getString(ID);

            MovieParsedData[i] = "("+poster_path+")"+"("+title+")" +"("+date_released+")"+"("+overview+")"+"("+ backdrop_path + ")"+"("+vote_average_rating+")";

        }

        return MovieParsedData;
    }

}
