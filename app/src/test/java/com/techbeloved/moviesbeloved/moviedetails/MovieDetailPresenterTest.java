package com.techbeloved.moviesbeloved.moviedetails;

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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieDetailPresenterTest {

    @Mock
    private MoviesRepository mMoviesRepository;

    @Mock
    private MovieEntity mMovie;

    @Mock
    private MovieDetailContract.View mDetailView;

    @Captor
    private ArgumentCaptor<MoviesDataSource.GetMovieCallback> mGetMovieCallback;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    private MovieDetailPresenter mDetailPresenter;

    private int mMovieId = 123;

    @Before
    public void setUp() throws Exception {
        // Get a refeerence to the class under test
        mDetailPresenter = new MovieDetailPresenter(mMovieId, mMoviesRepository, mDetailView);
        verify(mDetailView).setPresenter(mDetailPresenter);
    }

    @Test
    public void getMovieFromRepositoryAndLoadIntoView() {
        mDetailPresenter.start();
        verify(mMoviesRepository).getMovie(eq(mMovieId), mGetMovieCallback.capture());

        mGetMovieCallback.getValue().onMovieLoaded(mMovie);

        InOrder inOrder = Mockito.inOrder(mDetailView);

        inOrder.verify(mDetailView).setLoadingIndicator(true);
        inOrder.verify(mDetailView).setLoadingIndicator(false);

        verify(mDetailView).showMovieDetail(mMovie);
    }

    @Test
    public void getUnknownMovieFromRepository() {
        int invalidId = -1;
        mDetailPresenter.start();
        verify(mMoviesRepository).getMovie(eq(invalidId), mGetMovieCallback.capture());

        mGetMovieCallback.getValue().onDataNotAvailable();
//        verify(mDetailView).showError();
    }

    @Test
    public void addMovieToFavoriteIsRequested() {
        mDetailPresenter.setCurrentMovie(mMovie);
        when(mMovie.isFavorite()).thenReturn(false);

        mDetailPresenter.toggleFavorite();

        verify(mMoviesRepository).saveMovie(mMovie);
        verify(mDetailView).showMovieFavorited();
    }

    @Test
    public void removeMovieFromFavoritesIsRequested() {

    }

    @Test
    public void playVideoPromptsViewToLoadVideo() {

    }
}