package com.orie.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieDetailActivity extends AppCompatActivity {
    private String mMoviedetails,mtitle;

    private TextView original_title;
    private ImageView image;
    private TextView year;
    private TextView Rating;
    private TextView synopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        original_title = (TextView) findViewById(R.id.original_title);
        image = (ImageView) findViewById(R.id.image);
        year = (TextView) findViewById(R.id.year_of_release);
        Rating = (TextView) findViewById(R.id.rating);
        synopsis = (TextView) findViewById(R.id.synopsis);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {

            mMoviedetails = intent.getStringExtra(Intent.EXTRA_TEXT);

            List<String> list = new ArrayList<String>();
            Pattern regex = Pattern.compile("\\((.*?)\\)");
            Matcher regexMatcher = regex.matcher(mMoviedetails);

            String title = null;
            String posterPath = null;
            String datereleased = null;
            String overview = null;
            String backdrop = null;
            String rating = null;

            while (regexMatcher.find()) {
                list.add(regexMatcher.group(1));
            }

            posterPath = (list.get(0));
            title = (list.get(1));
            datereleased = (list.get(2));
            overview = (list.get(3));
            backdrop = (list.get(4));
            rating = (list.get(5));

            mtitle = title;

            Picasso.with(this).load(Uri.parse("http://image.tmdb.org/t/p/w300/" + backdrop)).into(image);
            original_title.setText(title);
            year.setText(datereleased);
            Rating.setText(rating);
            synopsis.setText(overview);

        }
    }


}
