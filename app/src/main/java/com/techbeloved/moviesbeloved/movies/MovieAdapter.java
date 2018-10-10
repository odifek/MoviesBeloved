package com.techbeloved.moviesbeloved.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.databinding.MovieItemBinding;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieEntity> mMovieList;
    final MovieClickCallback mMovieClickCallback;

    public MovieAdapter(MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
    }

    public void setMovieList(final List<MovieEntity> movieList) {
        if (movieList != null) {
            if (mMovieList == null) {
                mMovieList = movieList;
                notifyItemRangeInserted(0, movieList.size());
            } else {
                mMovieList.addAll(movieList);
                notifyItemRangeInserted(mMovieList.size(), movieList.size());
            }
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.movie_item, parent, false);
        binding.setCallback(mMovieClickCallback);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.binding.setMovie(mMovieList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMovieList != null ? mMovieList.size() : 0;
    }

    private Movie getItem(int position) {
        return mMovieList.get(position);
    }

    public void clear() {
        if (mMovieList != null) {
            mMovieList.clear();
            notifyDataSetChanged();
        }

    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        final MovieItemBinding binding;

        MovieViewHolder(@NonNull MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
