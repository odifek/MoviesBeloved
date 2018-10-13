package com.techbeloved.moviesbeloved.movies;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.databinding.MovieItemBinding;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    protected List<MovieEntity> mMovieList = new ArrayList<>();
    private final MovieClickCallback mMovieClickCallback;

    public MovieAdapter(MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
    }

    public void updateMovieList(final List<MovieEntity> movieList) {
        if (movieList != null) {
            if (mMovieList.isEmpty()) {
                mMovieList.addAll(movieList);
                notifyDataSetChanged();
            } else {
                updateItemsInternal(movieList);
            }
        }
    }

    private void updateItemsInternal(final List<MovieEntity> newMovies) {
        final List<MovieEntity> oldItems = new ArrayList<>(this.mMovieList);

        final MovieDiffCallback diffCallback = new MovieDiffCallback(oldItems, newMovies);

        final Handler handler = new Handler();
        new Thread(() -> {
            final DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(diffCallback);
            handler.post(() -> applyDiffResult(newMovies, diffResult));
        }).start();
    }

    private void applyDiffResult(List<MovieEntity> newMovies, DiffUtil.DiffResult diffResult) {
        dispatchUpdates(newMovies, diffResult);
    }

    private void dispatchUpdates(List<MovieEntity> newMovies, DiffUtil.DiffResult diffResult) {
        mMovieList.clear();
        mMovieList.addAll(newMovies);
        diffResult.dispatchUpdatesTo(this);
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
