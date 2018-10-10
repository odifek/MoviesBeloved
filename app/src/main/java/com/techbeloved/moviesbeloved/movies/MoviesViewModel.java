package com.techbeloved.moviesbeloved.movies;

import androidx.lifecycle.*;
import com.techbeloved.moviesbeloved.common.domain.LoadFavoriteMoviesUseCase;
import com.techbeloved.moviesbeloved.common.domain.LoadMoviesUseCase;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;
import io.reactivex.disposables.CompositeDisposable;

import java.util.List;

public class MoviesViewModel extends ViewModel {

    private final LoadFavoriteMoviesUseCase loadFavoriteMoviesUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Response<List<MovieEntity>>> response =
            new MutableLiveData<>();


    public MoviesViewModel(LoadFavoriteMoviesUseCase favoriteMoviesUseCase, SchedulersFacade schedulersFacade) {
        this.loadFavoriteMoviesUseCase = favoriteMoviesUseCase;
        this.schedulersFacade = schedulersFacade;

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    MutableLiveData<Response<List<MovieEntity>>> response() {
        return response;
    }

    void loadFavoriteMovies() {
        loadMovies(loadFavoriteMoviesUseCase);
    }

    private void loadMovies(LoadMoviesUseCase loadMoviesUseCase) {
        disposables.add(loadMoviesUseCase.execute()
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(__ -> response.setValue(Response.loading()))
                .subscribe(
                        movieEntities -> response.setValue(Response.success(movieEntities)),
                        throwable -> response.setValue(Response.error(throwable))
                )
        );
    }
}
