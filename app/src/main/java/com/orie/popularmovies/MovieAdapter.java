package com.orie.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Orie Victor on 4/15/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private String[] mMovieDataArray;
    private Context mContext ;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(String individualMovieData);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler handler) {
        mClickHandler = handler;
        this.mContext = context;
    }

    //Method to get the context
    public Context getContext(){
        return mContext;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView movieImage;

        public MovieViewHolder(View view){
            super(view);
            movieImage =(ImageView)view.findViewById(R.id.movie_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String eachMovie = mMovieDataArray[adapterPosition];
            mClickHandler.onClick(eachMovie);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int idForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(idForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieAdapterViewHolder, int position) {

        String originalTitle;
        String movieposterPath = null;
        String movieOverview;
        String movieReleaseDate;
        String VoteAverage = null;

        String MovieDatapositon = mMovieDataArray[position];

        List<String> list = new ArrayList<String>();
        //g this
        Pattern regex = Pattern.compile("\\((.*?)\\)");
        Matcher regexMatcher = regex.matcher(MovieDatapositon);

        String overview = null;
        String backdrop  = null;
        String user_rating  = null;
        String poster_path  = null;
        String movie_title  = null;
        String release_date  = null;


        while(regexMatcher.find()){
            list.add(regexMatcher.group(1));
        }

        poster_path = list.get(0);
        movie_title = list.get(1);
        release_date = list.get(2);
        overview = list.get(3);
        backdrop = list.get(4);
        user_rating = list.get(5);

        Picasso.with(mContext).load(Uri.parse("http://image.tmdb.org/t/p/w185/"+poster_path)).into(movieAdapterViewHolder.movieImage);

    }

    @Override
    public int getItemCount() {
        if (mMovieDataArray == null) return 0;
        return mMovieDataArray.length;
    }

    public void setmMovieDataArray(String[] movieData) {
        mMovieDataArray = movieData;
        notifyDataSetChanged();
    }
}

