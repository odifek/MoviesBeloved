package com.techbeloved.moviesbeloved.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.utils.EndlessScrollListener;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View {

    private MoviesContract.Presenter mPresenter;

    private MovieAdapter mAdapter;
    RecyclerView.OnScrollListener mOnScrollListener;
    RecyclerView mRecyclerView;

    private View mNoMoviesView;


    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment MoviesFragment.
     */
    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MovieAdapter(new MovieClickCallback() {
            @Override
            public void onClick(Movie movie) {
                // COMPLETED: 9/21/18 implement onclick
                mPresenter.openMovieDetails(movie.getId());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_movies, container, false);

        // Set up movies view

        mRecyclerView = root.findViewById(R.id.movie_list);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mOnScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPresenter.loadMoreMovies(page);
            }
        };
        mRecyclerView.setOnScrollListener(mOnScrollListener);

        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMovies(List<MovieEntity> movies) {
        mAdapter.setMovieList(movies);
    }

    @Override
    public void showMoreMovies(List<MovieEntity> movies) {
        mAdapter.setMovieList(movies);
    }

    @Override
    public void showLoadingMoviesError() {

    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public boolean isActive() {
        // Fragment is currently active
        return isAdded();
    }

    @Override
    public void showMovieDetails(int requestedMovieId) {
        // TODO: 9/21/18 create intent and launch the MovieDetail activity
    }
}
