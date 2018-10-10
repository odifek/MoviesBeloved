package com.techbeloved.moviesbeloved.movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.techbeloved.moviesbeloved.common.domain.LoadFavoriteMoviesUseCase;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;

public class MoviesViewModelFactory implements ViewModelProvider.Factory {

    private final LoadFavoriteMoviesUseCase loadFavoriteMoviesUseCase;

    private final SchedulersFacade schedulersFacade;

    public MoviesViewModelFactory(LoadFavoriteMoviesUseCase loadFavoriteMoviesUseCase, SchedulersFacade schedulersFacade) {
        this.loadFavoriteMoviesUseCase = loadFavoriteMoviesUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesViewModel.class)) {
            return (T) new MoviesViewModel(loadFavoriteMoviesUseCase, schedulersFacade);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
