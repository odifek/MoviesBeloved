package com.techbeloved.moviesbeloved.movies;

import androidx.recyclerview.widget.DiffUtil;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

public class MovieDiffCallback extends DiffUtil.Callback {
    private final List<MovieEntity> mOldMovieList;
    private final List<MovieEntity> mNewMovieList;

    public MovieDiffCallback(List<MovieEntity> oldMovieList, List<MovieEntity> newMovieList) {
        this.mOldMovieList = oldMovieList;
        this.mNewMovieList = newMovieList;
    }

    @Override
    public int getOldListSize() {
        return mOldMovieList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMovieList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldMovieList.get(oldItemPosition).getId()
                == mNewMovieList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final MovieEntity oldMovie = mOldMovieList.get(oldItemPosition);
        final MovieEntity newMovie = mNewMovieList.get(newItemPosition);

        return oldMovie.getTitle().equals(newMovie.getTitle());
    }
}
