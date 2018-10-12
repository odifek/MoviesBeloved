package com.techbeloved.moviesbeloved.movies;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.databinding.MovieItemBinding;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieEntity> mMovieList;
    final MovieClickCallback mMovieClickCallback;

    public MovieAdapter(MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
    }

    public void setMovieList(final List<MovieEntity> movieList) {
        if (movieList != null) {
            mMovieList = movieList;
            notifyDataSetChanged();
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
