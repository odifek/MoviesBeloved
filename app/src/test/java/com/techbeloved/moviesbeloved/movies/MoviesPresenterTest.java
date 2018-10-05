package com.techbeloved.moviesbeloved.movies;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoviesPresenterTest {

    @Mock
    private MoviesRepository mMoviesRepository;

    @Mock
    private MovieEntity mMovie;

    @Mock
    private MoviesContract.View mMoviesView;

    @Captor
    private ArgumentCaptor<MoviesDataSource.LoadMoviesCallback> mLoadMoviesCallback;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    private MoviesPresenter mMoviesPresenter;

    @Before
    public void setUp() throws Exception {
        // Get a reference to the class under test
        mMoviesPresenter = new MoviesPresenter(mMoviesRepository, mMoviesView);
        verify(mMoviesView).setPresenter(mMoviesPresenter);
    }


    @Test
    public void loadMoviesFromRepositoryAndLoadIntoView() {
        // Check that view is active
        when(mMoviesView.isActive()).thenReturn(true);
        // Create mocked list of Movies
        List<MovieEntity> movies = Arrays.asList(mMovie, mMovie, mMovie, mMovie);

        mMoviesPresenter.setFiltering(MovieFilterType.POPULAR);
        mMoviesPresenter.loadMovies(true);
        verify(mMoviesRepository).getMovies(eq(MovieFilterType.POPULAR), mLoadMoviesCallback.capture());
        mLoadMoviesCallback.getValue().onMoviesLoaded(movies);

        // Checks that all commands are run one after the other
        InOrder inOrder = Mockito.inOrder(mMoviesView);

        inOrder.verify(mMoviesView).setLoadingIndicator(true);
        inOrder.verify(mMoviesView).setLoadingIndicator(false);
        verify(mMoviesView).showMovies(movies);
    }

    @Test
    public void loadMoviesFromRepositoryReportsNoDataAvailable() {
        // Check that view is active
        when(mMoviesView.isActive()).thenReturn(true);
        mMoviesPresenter.setFiltering(MovieFilterType.POPULAR);
        mMoviesPresenter.loadMovies(true);
        verify(mMoviesRepository).getMovies(eq(MovieFilterType.POPULAR), mLoadMoviesCallback.capture());

        mLoadMoviesCallback.getValue().onDataNotAvailable();

        InOrder inOrder = Mockito.inOrder(mMoviesView);
        inOrder.verify(mMoviesView).setLoadingIndicator(true);
        inOrder.verify(mMoviesView).setLoadingIndicator(false);
        verify(mMoviesView).showNoMovies();
    }

    @Test
    public void loadMoreMoviesFromRepositoryAndLoadIntoView() {
        // Check that view is active
        when(mMoviesView.isActive()).thenReturn(true);
        // Create mocked list of Movies
        List<MovieEntity> movies = Arrays.asList(mMovie, mMovie, mMovie, mMovie);
        int pageToLoad = 2;
        mMoviesPresenter.setFiltering(MovieFilterType.POPULAR);
        mMoviesPresenter.loadMoreMovies(pageToLoad);
        verify(mMoviesRepository).getMovies(eq(MovieFilterType.POPULAR), eq(pageToLoad), mLoadMoviesCallback.capture());

        mLoadMoviesCallback.getValue().onMoviesLoaded(movies);
        verify(mMoviesView).showMoreMovies(movies);
    }

    @Test
    public void loadMoreMoviesFromRepositoryReportsNoDataAvailable() {
        // Check that view is active
        when(mMoviesView.isActive()).thenReturn(true);
        mMoviesPresenter.setFiltering(MovieFilterType.POPULAR);
        mMoviesPresenter.loadMovies(true);
        verify(mMoviesRepository).getMovies(eq(MovieFilterType.POPULAR), mLoadMoviesCallback.capture());

        mLoadMoviesCallback.getValue().onDataNotAvailable();
    }

    @Test
    public void openMovieDetails_shouldShowMovieDetails() {
        int movieId = 123;
        mMoviesPresenter.openMovieDetails(movieId);
        verify(mMoviesView).showMovieDetails(movieId);
    }

}