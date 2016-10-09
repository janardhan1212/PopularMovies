package com.janardhan_y.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by janardhanyerranagu on 09/10/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        CardView cardview;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardview = (CardView) view.findViewById(R.id.card_view);
        }
    }


    public MoviesAdapter(Activity mContext, List<Movie> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Movie album = albumList.get(position);
        holder.title.setText(album.getTitle());
        holder.count.setText(album.getOriginalLanguage());
        Picasso.with(mContext).load(AppConstants.image_url + album.getPosterPath()).into(holder.thumbnail);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,MovieDetailsActivity.class);
                intent.putExtra("movie_id",album.getId());
                mContext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}