package com.techbeloved.moviesbeloved.moviedetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;

import javax.inject.Inject;

public class MovieDetailViewModelFactory implements ViewModelProvider.Factory {
    private final MoviesRepository moviesRepository;

    private final SchedulersFacade schedulersFacade;

    @Inject
    public MovieDetailViewModelFactory(MoviesRepository moviesRepository,
                                       SchedulersFacade schedulersFacade) {
        this.moviesRepository = moviesRepository;
        this.schedulersFacade = schedulersFacade;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieDetailViewModel.class)) {
            return (T) new MovieDetailViewModel(moviesRepository, schedulersFacade);
        }
        throw new IllegalArgumentException("Unknown model class");
    }
}
