package com.techbeloved.moviesbeloved.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.models.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<? extends Movie> mMovieList;
    final MovieClickCallback mMovieClickCallback;

    public MovieAdapter(MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
    }

    public void setMovieList(final List<? extends Movie> movieList) {
        if (mMovieList == null) {
            mMovieList = movieList;
            notifyItemRangeInserted(0, movieList.size());
        } else {
            mMovieList = movieList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        // No data yet
        if (mMovieList == null) return;

        Movie movie = getItem(position);

        holder.movieTitleText.setText(movie.getTitle());

        String rating = String.valueOf(movie.getUserRating());
        holder.movieRatingText.setText(rating);

        String posterUrl = movie.getPosterUrl();
        if (posterUrl != null) {
            Glide.with(holder.moviePosterImage.getContext())
                    .load(posterUrl)
                    .into(holder.moviePosterImage);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList != null ? mMovieList.size() : 0;
    }

    private Movie getItem(int position) {
        return mMovieList.get(position);
    }
}
