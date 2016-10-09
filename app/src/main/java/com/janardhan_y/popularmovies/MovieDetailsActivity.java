package com.janardhan_y.popularmovies;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    ProgressBar progressBar;
    private LinearLayout detailsLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView overview;
    private TextView rating,relese_date;
    private ImageView poster;

    private String TAG="MovieDeatis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int movie_id = getIntent().getExtras().getInt("movie_id");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        detailsLayout = (LinearLayout) findViewById(R.id.detailsLayout);
        collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        overview = (TextView) findViewById(R.id.overview);
        poster = (ImageView) findViewById(R.id.poster);
        rating = (TextView) findViewById(R.id.rating);
        relese_date=(TextView) findViewById(R.id.relese_date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Movie> movie_call = apiService.getMovieDetails(movie_id, AppConstants.api_key);
        LoadData(movie_call);
    }

    private void LoadData(Call<Movie> l_call) {
        detailsLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        l_call.clone().enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                int statusCode = response.code();
                detailsLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                  Movie movie = response.body();
                collapsingToolbar.setTitle(movie.getTitle());
                overview.setText(movie.getOverview());
                rating.setText(String.valueOf(movie.getVoteAverage()));
                Picasso.with(MovieDetailsActivity.this).load(AppConstants.big_image_url + movie.getBackdropPath()).into(poster);
                relese_date.setText(movie.getReleaseDate());


            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Toast.makeText(MovieDetailsActivity.this, "Network Problem !!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
