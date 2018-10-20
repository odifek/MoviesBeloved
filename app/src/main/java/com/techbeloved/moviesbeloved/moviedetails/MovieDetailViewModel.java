package com.techbeloved.moviesbeloved.moviedetails;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class MovieDetailViewModel extends ViewModel {

    public ObservableField<MovieEntity> movie = new ObservableField<>();
    public ObservableBoolean dataLoading = new ObservableBoolean();
    public ObservableBoolean reviewsLoading = new ObservableBoolean();
    public ObservableBoolean videosLoading = new ObservableBoolean();
    public ObservableBoolean isFavorite = new ObservableBoolean();

    private MoviesRepository mMoviesRepository;
    private SchedulersFacade mSchedulersFacade;

    private CompositeDisposable disposables = new CompositeDisposable();
    private int movieId;

    private MutableLiveData<Response<MovieEntity>> movieResponse = new MutableLiveData<>();
    private MutableLiveData<Response<List<ReviewEntity>>> reviewsResponse = new MutableLiveData<>();
    private MutableLiveData<Response<List<VideoEntity>>> videosResponse = new MutableLiveData<>();

    @Inject
    MovieDetailViewModel(MoviesRepository moviesRepository, SchedulersFacade schedulersFacade) {
        mMoviesRepository = moviesRepository;
        mSchedulersFacade = schedulersFacade;
    }

    public void start() {
        loadMovie();
        loadReviews();
        loadVideos();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public MutableLiveData<Response<List<ReviewEntity>>> getReviewsResponse() {
        return reviewsResponse;
    }

    public MutableLiveData<Response<List<VideoEntity>>> getVideosResponse() {
        return videosResponse;
    }

    public MutableLiveData<Response<MovieEntity>> getMovieResponse() {
        return movieResponse;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    private void loadMovie() {
        disposables.add(mMoviesRepository.getMovie(movieId)
                .subscribeOn(mSchedulersFacade.io())
                .observeOn(mSchedulersFacade.ui())
                .doOnSubscribe(__ -> {
                    dataLoading.set(true);
                    movieResponse.setValue(Response.loading());
                })
                .subscribe(movieEntity -> {
                    dataLoading.set(false);
                    movieResponse.setValue(Response.success(movieEntity));
                    movie.set(movieEntity);
                }, throwable -> {
                    dataLoading.set(false);
                    movieResponse.setValue(Response.error(throwable));
                }));

    }

    private void loadReviews() {
        disposables.add(mMoviesRepository.getReviews(movieId)
                .subscribeOn(mSchedulersFacade.io())
                .observeOn(mSchedulersFacade.ui())
                .doOnSubscribe(__ -> {
                    reviewsLoading.set(true);
                    reviewsResponse.setValue(Response.loading());
                })
                .subscribe(reviewEntities -> {
                    reviewsLoading.set(false);
                    reviewsResponse.setValue(Response.success(reviewEntities));
                }, throwable -> {
                    reviewsLoading.set(false);
                    reviewsResponse.setValue(Response.error(throwable));
                })
        );
    }

    private void loadVideos() {
        disposables.add(mMoviesRepository.getVideos(movieId)
                .subscribeOn(mSchedulersFacade.io())
                .observeOn(mSchedulersFacade.ui())
                .doOnSubscribe(__ -> {
                    videosLoading.set(true);
                    videosResponse.setValue(Response.loading());
                })
                .subscribe(videoEntities -> {
                    videosLoading.set(false);
                    videosResponse.setValue(Response.success(videoEntities));
                }, throwable -> {
                    videosLoading.set(false);
                    videosResponse.setValue(Response.error(throwable));
                })
        );
    }

    private void saveCurrentMovie() {
        mMoviesRepository.saveMovie(Objects.requireNonNull(movie.get()));
    }

    void toggleFavorite() {
        if (Objects.requireNonNull(movie.get()).isFavorite()) {
            Objects.requireNonNull(movie.get()).setFavorite(false);
            removeFromFavorites(movie.get().getId());
            isFavorite.set(false);
        } else {
            Objects.requireNonNull(movie.get()).setFavorite(true);
            saveCurrentMovie();
            isFavorite.set(true);
        }
    }

    private void removeFromFavorites(int movieId) {
        mMoviesRepository.deleteMovie(movieId);
    }
}
