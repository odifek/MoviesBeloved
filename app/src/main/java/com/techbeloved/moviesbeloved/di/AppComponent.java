package com.techbeloved.moviesbeloved.di;

import android.app.Application;
import com.techbeloved.moviesbeloved.MoviesApp;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.data.source.MoviesRepositoryModule;
import com.techbeloved.moviesbeloved.data.source.remote.NetworkModule;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailModule;
import com.techbeloved.moviesbeloved.movies.ViewModelModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityBindinggModule.class,
        MoviesRepositoryModule.class,
        ViewModelModule.class,
        NetworkModule.class,
        MovieDetailModule.class
})
public interface AppComponent extends AndroidInjector<MoviesApp> {

    MoviesRepository getMoviesRepository();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
