package com.techbeloved.moviesbeloved.movies;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.databinding.MovieItemBinding;

import timber.log.Timber;

public class MovieAdapter extends PagedListAdapter<MovieEntity, MovieAdapter.MovieViewHolder> {
    private final MovieClickCallback mMovieClickCallback;

    private static final DiffUtil.ItemCallback<MovieEntity> MOVIE_ENTITY_ITEM_CALLBACK = new DiffUtil.ItemCallback<MovieEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieEntity oldItem, @NonNull MovieEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieEntity oldItem, @NonNull MovieEntity newItem) {
            if (oldItem.getTitle() != null) {
                return oldItem.getTitle().equals(newItem.getTitle());
            } else {
                return false;
            }
        }
    };

    MovieAdapter(MovieClickCallback clickCallback) {
        super(MOVIE_ENTITY_ITEM_CALLBACK);
        this.mMovieClickCallback = clickCallback;
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
        if (position == 19) {
            Timber.i("About last item");
        }
        holder.binding.setMovie(getItem(position));
        holder.binding.executePendingBindings();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        final MovieItemBinding binding;

        MovieViewHolder(@NonNull MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
