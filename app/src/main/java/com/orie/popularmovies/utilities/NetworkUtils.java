package com.orie.popularmovies.utilities;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Orie Victor on 4/16/2017.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();



    /**
     * Builds the URL used to query Github.
     *
     * @param movieQueryUrl The keyword that will be queried for.
     * @return The URL to use to query the the movie db server.
     */
    public static URL buildUrl(String movieQueryUrl) {
        String movieUrl = "";
        //here we check for the boolean sort oder and determine which URL query are we to use
        if (movieQueryUrl.equals("true")){
            movieUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=ef2b6d18f9b7fdc528b00a6b0f13ee7a";
        }else {
            movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=ef2b6d18f9b7fdc528b00a6b0f13ee7a";
        }
        Uri builtUri = Uri.parse(movieUrl).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     */
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
