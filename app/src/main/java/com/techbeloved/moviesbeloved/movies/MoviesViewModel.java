package com.techbeloved.moviesbeloved.movies;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import javax.inject.Inject;

public class MoviesViewModel extends ViewModel {

    // set default filter type
    private final ObservableField<MovieFilterType> filterTypeObservable =
            new ObservableField<>(MovieFilterType.POPULAR);

    private final MoviesRepository mMoviesRepository;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Response<PagedList<MovieEntity>>> response =
            new MutableLiveData<>();
    // We use this to collect paginated data

    private final PublishSubject<MovieFilterType> filterSubject = PublishSubject.create();

    @Inject
    public MoviesViewModel(MoviesRepository moviesRepository,
                           SchedulersFacade schedulersFacade) {
        this.mMoviesRepository = moviesRepository;
        this.schedulersFacade = schedulersFacade;

        filterTypeObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                filterSubject.onNext(getCurrentFilter());
            }
        });

        loadMovies();
    }


    @Override
    protected void onCleared() {
        Timber.i("MoviesViewmodel onCleared() is called! Why?");
        disposables.clear();
    }

    LiveData<Response<PagedList<MovieEntity>>> response() {
        return response;
    }

    /**
     * Load the movies according to the filter supplied. Request for favorites is forwarded to the data source that handles local data,
     * while non -favorites request are forwarded to the corresponding online data source
     */
    private void loadMovies() {
        Disposable disposable = filterSubject
                .startWith(getCurrentFilter())
                .publish(filterType -> io.reactivex.Observable.merge(
                        filterType.filter(type -> type == MovieFilterType.FAVORITES).compose(getFavoriteMovies()),
                        filterType.filter(type -> type != MovieFilterType.FAVORITES).compose(getOnlineMovies())
                ))
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(response::setValue,
                        throwable -> response.postValue(Response.error(throwable)));

        disposables.add(disposable);
    }

    private ObservableTransformer<MovieFilterType, Response<PagedList<MovieEntity>>> getOnlineMovies() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(60)
                .build();
        return upstream -> upstream.switchMap(filter -> new RxPagedListBuilder<>(
                new MoviesPagingDataSource.MoviesPagingFactory(getCurrentFilter(), mMoviesRepository),
                config)
                .buildObservable()
                .map(Response::success)
                .startWith(Response.loading()));
    }

    private ObservableTransformer<MovieFilterType, Response<PagedList<MovieEntity>>> getFavoriteMovies() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        return upstream -> upstream.switchMap(filter -> new RxPagedListBuilder<>(
                new FavoritesDataSource.FavoritesDataSourceFactory(mMoviesRepository),
                config
        ).buildObservable()
                .map(Response::success)
                .startWith(Response.loading()));
    }

    private MovieFilterType getCurrentFilter() {
        return filterTypeObservable.get();
    }

    void setFilterType(MovieFilterType filterType) {
        filterTypeObservable.set(filterType);
    }


    MovieFilterType getFiltering() {
        return getCurrentFilter();
    }
}
