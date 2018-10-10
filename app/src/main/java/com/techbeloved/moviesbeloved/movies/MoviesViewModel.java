package com.techbeloved.moviesbeloved.movies;

import androidx.lifecycle.*;
import com.techbeloved.moviesbeloved.common.domain.LoadFavoriteMoviesUseCase;
import com.techbeloved.moviesbeloved.common.domain.LoadMoviesUseCase;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import java.util.List;

public class MoviesViewModel extends ViewModel {

    private final LoadFavoriteMoviesUseCase loadFavoriteMoviesUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Response<List<MovieEntity>>> response =
            new MutableLiveData<>();

//    private final MediatorLiveData<Response<List<MovieEntity>>> mObservableMovies;


    public MoviesViewModel(LoadFavoriteMoviesUseCase favoriteMoviesUseCase, SchedulersFacade schedulersFacade) {
        this.loadFavoriteMoviesUseCase = favoriteMoviesUseCase;
        this.schedulersFacade = schedulersFacade;

//        mObservableMovies = new MediatorLiveData<>();
//        mObservableMovies.setValue(Response.loading());
////        response.setValue(Response.loading());
//        mObservableMovies.addSource(favoriteMoviesUseCase.execute(), (List<MovieEntity> value) -> {
//            mObservableMovies.setValue(Response.success(value));
////            response.setValue(Response.success(value));
//        });
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    MutableLiveData<Response<List<MovieEntity>>> response() {
        return response;
    }
//
//    MediatorLiveData<Response<List<MovieEntity>>> getMovies() {
//        return mObservableMovies;
//    }

    void loadFavoriteMovies() {
        Timber.i("load favorites called in viewmodel");
        loadMovies(loadFavoriteMoviesUseCase);
    }

    private void loadMovies(LoadMoviesUseCase loadMoviesUseCase) {
        Timber.i("Observing some data");

        disposables.add(loadMoviesUseCase.execute()
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(__ -> response.setValue(Response.loading()))
                .subscribe(
                        movieEntities -> {
                            Timber.i("New data coming in viewmodel");
                            response.setValue(Response.success(movieEntities));
                        },
                        throwable -> response.setValue(Response.error(throwable))
                )
        );
    }
}
