package com.janardhan_y.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private boolean popular = true;
    private Call<MoviesResponse> top_call;
    private Call<MoviesResponse> popular_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        top_call = apiService.getTopRatedMovies(AppConstants.api_key);
        popular_call= apiService.getPopularMovies(AppConstants.api_key);
        Toast.makeText(MainActivity.this, "Sorting as Top  most popular", Toast.LENGTH_LONG).show();
        LoadData(popular_call);
    }

    private void LoadData(Call<MoviesResponse> l_call) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        l_call.clone().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Toast.makeText(MainActivity.this, "Network Problem !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            if (popular == true) {
                popular=false;
                Toast.makeText(MainActivity.this, "Sorting as Top highest-rated", Toast.LENGTH_LONG).show();
                LoadData(top_call);
            } else {
                popular=true;
                Toast.makeText(MainActivity.this, "Sorting as Top  most popular", Toast.LENGTH_LONG).show();
                LoadData(popular_call);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
