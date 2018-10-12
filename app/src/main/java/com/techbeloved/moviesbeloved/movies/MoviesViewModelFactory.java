package com.techbeloved.moviesbeloved.movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoviesViewModelFactory implements ViewModelProvider.Factory {

    private final MoviesRepository moviesRepository;

    private final SchedulersFacade schedulersFacade;

    @Inject
    public MoviesViewModelFactory(MoviesRepository moviesRepository,
                                  SchedulersFacade schedulersFacade) {
        this.moviesRepository = moviesRepository;
        this.schedulersFacade = schedulersFacade;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesViewModel.class)) {
            return (T) new MoviesViewModel(moviesRepository, schedulersFacade);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
