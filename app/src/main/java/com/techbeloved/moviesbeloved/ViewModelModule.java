package com.techbeloved.moviesbeloved;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.techbeloved.moviesbeloved.moviedetails.MovieDetailViewModel;
import com.techbeloved.moviesbeloved.movies.MoviesViewModel;
import com.techbeloved.moviesbeloved.movies.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface ViewModelModule {

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel.class)
    ViewModel bindMoviesViewModel(MoviesViewModel viewModel);@Binds

    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    ViewModel bindMoviesDetailViewModel(MovieDetailViewModel viewModel);
}
