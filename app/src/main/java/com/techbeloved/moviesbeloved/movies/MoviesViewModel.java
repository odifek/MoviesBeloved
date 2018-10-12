package com.techbeloved.moviesbeloved.movies;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends ViewModel {

    // set default filter type
    private final ObservableField<MovieFilterType> filterTypeObservable =
            new ObservableField<>(MovieFilterType.POPULAR);

    private final ObservableInt currentPage = new ObservableInt(1);

    private final MoviesRepository mMoviesRepository;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Response<List<MovieEntity>>> response =
            new MutableLiveData<>();
    // We use this to collect paginated data
    private final MutableLiveData<Response<List<MovieEntity>>> movieCollectionResponse = new MutableLiveData<>();

    @Inject
    public MoviesViewModel(MoviesRepository moviesRepository,
                           SchedulersFacade schedulersFacade) {
        this.mMoviesRepository = moviesRepository;
        this.schedulersFacade = schedulersFacade;

        // Initialise the movie collection
        movieCollectionResponse.setValue(Response.loading());

        filterTypeObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                reloadMovies();
            }
        });

        currentPage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                disposables.clear();
                loadNextPage();
            }
        });

        loadMovies();
    }

    private void loadNextPage() {
        Timber.i("LoadNextPage - did I get called?");
        if (getCurrentFilter() != MovieFilterType.FAVORITES) {
            loadMovies();
        }
    }

    @Override
    protected void onCleared() {
        Timber.i("MoviesViewmodel onCleared() is called! Why?");
        disposables.clear();
    }

    MutableLiveData<Response<List<MovieEntity>>> response() {
        return response;
    }

    MutableLiveData<Response<List<MovieEntity>>> getMovieCollectionResponse() {
        return movieCollectionResponse;
    }

    private void reloadMovies() {
        // Reset collection
        movieCollectionResponse.setValue(Response.loading());
        disposables.clear();
        // This should trigger loadNextPage which succeeds only if online movie
        // case requested and current page is above 1
        if (getCurrentFilter() == MovieFilterType.FAVORITES || currentPage.get() == 1) {
            loadMovies();
        } else {
            // trigger next page load
            resetPage();
        }
    }

    private void resetPage() {
        currentPage.set(1);
    }

    private void loadMovies() {
        Timber.i("Observing some data");

        disposables.add(mMoviesRepository.getMovies(getCurrentFilter(), currentPage.get())
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(__ -> response.setValue(Response.loading()))
                .subscribe(
                        movieEntities -> {
                            Timber.i("New data coming in viewmodel of length - %s", movieEntities.size());
                            response.setValue(Response.success(movieEntities));
                            addToMoviesCollection(movieEntities);
                        },
                        throwable -> response.setValue(Response.error(throwable))
                )
        );
    }

    private MovieFilterType getCurrentFilter() {
        return filterTypeObservable.get();
    }

    public void setFilterType(MovieFilterType filterType) {
        filterTypeObservable.set(filterType);
    }

    private void addToMoviesCollection(List<MovieEntity> movies) {
//        Objects.requireNonNull(Objects.requireNonNull(movieCollectionResponse.getValue()).data).addAll(movies);
        // This triggers the observers to do something. Only set value can do that!
        Response<List<MovieEntity>> listResponse = movieCollectionResponse.getValue();
        List<MovieEntity> movieEntityList = listResponse.data;
        if (movieEntityList == null) {
            movieEntityList = new ArrayList<>();
        }
        movieEntityList.addAll(movies);

        movieCollectionResponse.setValue(Response.success(movieEntityList));
    }

    public MovieFilterType getFiltering() {
        return getCurrentFilter();
    }

    public void setNextPage(int page) {
        currentPage.set(page);
    }

    public int getCurrentPage() {
        return currentPage.get();
    }
}
