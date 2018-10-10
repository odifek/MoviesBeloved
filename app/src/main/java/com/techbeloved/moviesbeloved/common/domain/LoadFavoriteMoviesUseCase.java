package com.techbeloved.moviesbeloved.common.domain;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

public class LoadFavoriteMoviesUseCase implements LoadMoviesUseCase {
    private final MoviesRepository moviesRepository;

    public LoadFavoriteMoviesUseCase(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public Flowable<List<MovieEntity>> execute() {
        return moviesRepository.getMovies(MovieFilterType.FAVORITES);
    }
}
