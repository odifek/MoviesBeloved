package com.techbeloved.moviesbeloved.movies;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.MoviesApp;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private MediatorLiveData<List<MovieEntity>> mObservableMovies;


    public MoviesViewModel(@NonNull Application application) {
        super(application);

        mObservableMovies = new MediatorLiveData<>();
        mObservableMovies.setValue(null);

        LiveData<List<MovieEntity>> movies = ((MoviesApp) application).getRepository()
                .getMovies(MovieFilterType.FAVORITES);

        // observe the changes of the movies from the database and forward them
        mObservableMovies.addSource(movies, mObservableMovies::setValue);
    }

    public LiveData<List<MovieEntity>> getMovies() {
        return mObservableMovies;
    }
}
