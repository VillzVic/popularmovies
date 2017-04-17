package com.orie.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orie.popularmovies.data.MoviePreferences;
import com.orie.popularmovies.utilities.MovieJsonUtils;
import com.orie.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static boolean PREFERENCES_UPDATED = false;
    private  int MOVIE_LOADER_ID = 0;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting references to the views
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //using a layout manager to position items
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
 /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
         /*
         * The MovieAdapter is responsible for linking the movie data with the Views that
         * will end up displaying each movie data.
         */
        mMovieAdapter = new MovieAdapter(this,this);

        /* Attaching the recyclerview to the adapter*/
        mRecyclerView.setAdapter(mMovieAdapter);

        //check if Internet is available
        if(!isInternetAvailable(getBaseContext())){
            mErrorMessageDisplay.setText(getString(R.string.no_internet));
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();

        String isHighRatedValue = "";
        boolean isHighRated = MoviePreferences.isRatedHigh(MainActivity.this);
        if (isHighRated){
            isHighRatedValue = "true";
        }else {
            isHighRatedValue = "false";
        }
        new FetchMovieTask().execute(isHighRatedValue);
    }
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the preferences for sort order has changed, set it back to false
        if (PREFERENCES_UPDATED){
            Log.d(TAG, "preferences have been updated");
//            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            PREFERENCES_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
          /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }



    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onClick(String individualMovieData) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, MovieDetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, individualMovieData);
        startActivity(intentToStartDetailActivity);
    }

    class FetchMovieTask extends AsyncTask<String, Void, String[]>{
        /* This String array will reference our movie data */
        String[] mMovieData = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mMovieData != null) {
                sendResult(mMovieData);
            } else {
                mLoadingIndicator.setVisibility(View.VISIBLE);

            }
        }



        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String isHighRatedValue = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(isHighRatedValue);
            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                String[] simpleJsonMovieData = MovieJsonUtils.getMovieFromJson(MainActivity.this, jsonMovieResponse);


                return simpleJsonMovieData;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String[] Moviedata) {
            super.onPostExecute(Moviedata);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieAdapter.setmMovieDataArray(Moviedata);
            if (Moviedata == null) {
                showErrorMessage();
            } else {
                showMovieDataView();
            }
        }

        //send the result of the load to the listener
        public void sendResult(String[] data) {
            mMovieData = data;
//            super.sendResult(data);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
         /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.popular_movies){

            new FetchMovieTask().execute("false");
        }else if(id == R.id.top_rated){
            new FetchMovieTask().execute("true");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*
         * Set this flag to true so that when control returns to MainActivity, it can refresh the
         * data.
         */
        PREFERENCES_UPDATED = true;
    }

}
