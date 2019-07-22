package com.techbeloved.moviesbeloved.di;

import android.app.Application;
import android.content.Context;

import com.techbeloved.moviesbeloved.ViewModelModule;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.data.source.MoviesRepositoryModule;
import com.techbeloved.moviesbeloved.data.source.remote.NetworkModule;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;
import com.techbeloved.moviesbeloved.movies.MoviesFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        MoviesRepositoryModule.class,
        ViewModelModule.class,
        NetworkModule.class
})
public interface AppComponent {

    void inject(MoviesFragment moviesFragment);

    void inject(MovieDetailActivity movieDetailActivity);


    MoviesRepository getMoviesRepository();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Context application);

        AppComponent build();
    }
}
