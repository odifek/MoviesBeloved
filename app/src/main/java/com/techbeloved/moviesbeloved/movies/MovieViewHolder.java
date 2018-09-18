package com.techbeloved.moviesbeloved.movies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techbeloved.moviesbeloved.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    ImageView moviePosterImage;
    TextView movieTitleText;
    TextView movieRatingText;
    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        moviePosterImage = itemView.findViewById(R.id.iv_movie_poster);
        movieTitleText = itemView.findViewById(R.id.tv_title_movie);
        movieRatingText = itemView.findViewById(R.id.tv_rating_value);
    }
}
