package com.techbeloved.moviesbeloved.common.domain;

import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import io.reactivex.Flowable;

import java.util.List;

public interface LoadMoviesUseCase {
    Flowable<List<MovieEntity>> execute();
}
