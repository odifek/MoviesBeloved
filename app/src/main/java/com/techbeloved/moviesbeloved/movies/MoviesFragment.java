package com.techbeloved.moviesbeloved.movies;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;
import com.techbeloved.moviesbeloved.utils.EndlessScrollListener;

import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_ID_EXTRA;

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

    private TextView mNoMoviesTextView;
    private ContentLoadingProgressBar mProgressBar;

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

        mNoMoviesTextView = root.findViewById(R.id.empty_textview);
        mProgressBar = root.findViewById(R.id.loading_progressbar);

        mRecyclerView = root.findViewById(R.id.movie_list);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mOnScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPresenter.loadMoreMovies(page + 1); // The first page is actually 1 not 0, so increment to match
            }
        };
        mRecyclerView.setOnScrollListener(mOnScrollListener);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment_menu, menu);
        // Initialise menu items
        switch (mPresenter.getFiltering()) {
            case TOP_RATED:
                menu.findItem(R.id.top_rated_filter_menu).setChecked(true);
                break;
            case POPULAR:
                menu.findItem(R.id.popularity_filter_menu).setChecked(true);
                break;
            case FAVORITES:
                menu.findItem(R.id.favorites_filter_menu).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.top_rated_filter_menu:
                mPresenter.setFiltering(MovieFilterType.TOP_RATED);
                if (!item.isChecked()) {
                    // Only reload if the it's not the currently selected filter
                    mAdapter.clear();
                    mPresenter.loadMovies();
                }
                break;
            case R.id.popularity_filter_menu:
                mPresenter.setFiltering(MovieFilterType.POPULAR);
                if (!item.isChecked()) {
                    mAdapter.clear();
                    mPresenter.loadMovies();
                }
                break;
            case R.id.favorites_filter_menu:
                mPresenter.setFiltering(MovieFilterType.FAVORITES);
                if (!item.isChecked()) {
                    Timber.i("Should reload");
                    mAdapter.clear();
                    mPresenter.loadMovies();
                }
                break;
        }
        // Finally check the item
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }

    @Override
    public void showMovies(List<MovieEntity> movies) {
        mNoMoviesTextView.setVisibility(View.VISIBLE);
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
        mNoMoviesTextView.setText("No movies to show");
        mNoMoviesTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        // Fragment is currently active
        return isAdded();
    }

    @Override
    public void showMovieDetails(int requestedMovieId) {
        // TODO: 9/21/18 create intent and launch the MovieDetail activity
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID_EXTRA, requestedMovieId);
        startActivity(intent);
    }
}
