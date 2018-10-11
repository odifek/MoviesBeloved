package com.techbeloved.moviesbeloved.common.domain;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import io.reactivex.Flowable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class LoadFavoriteMoviesUseCase implements LoadMoviesUseCase {
    private final MoviesRepository moviesRepository;

    @Inject
    public LoadFavoriteMoviesUseCase(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public Flowable<List<MovieEntity>> execute() {
        return moviesRepository.getMovies(MovieFilterType.FAVORITES);
    }
}
