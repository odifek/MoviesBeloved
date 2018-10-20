package com.techbeloved.moviesbeloved.moviedetails;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.techbeloved.moviesbeloved.movies.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class MovieDetailModule {
    @ContributesAndroidInjector
    abstract MovieDetailActivity movieDetailActivity();

//    @Provides
//    @MovieId
//    static int provideMovieId(MovieDetailActivity activity) {
//        return activity.getIntent().getIntExtra(Constants.MOVIE_ID_EXTRA, 0);
//    }

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(MovieDetailViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    abstract ViewModel moviesViewModel(MovieDetailViewModel viewModel);
}
