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

import androidx.annotation.Nullable;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;
import com.techbeloved.moviesbeloved.utils.EndlessScrollListener;

import java.util.List;

import androidx.annotation.NonNull;
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

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private static final String CURRENT_FILTERING = "CURRENT_FILTERING";
    private MoviesContract.Presenter mPresenter;
    private static MovieFilterType mCurrentFilter;

    private MovieAdapter mAdapter;
    RecyclerView.OnScrollListener mOnScrollListener;
    RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

    // Current page of movies list
    private int mCurrentPage;
    private static final String CURRENT_PAGE = "CURRENT_PAGE";

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

    /**
     * Any functionality that you would want to persist across pause and resume of fragment, for example,
     * when navigating to a new activity which you plan to return from,
     * such functionality should be place here in onCreate because it only gets executed once, on fragment creation
     * @param savedInstanceState is the saved instance bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate is called!");
        super.onCreate(savedInstanceState);
        mAdapter = new MovieAdapter(new MovieClickCallback() {
            @Override
            public void onClick(Movie movie) {
                // COMPLETED: 9/21/18 implement onclick
                mPresenter.openMovieDetails(movie.getId());
            }
        });
        mLayoutManager = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.movie_grid_span_count));
        mOnScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Timber.i("Loading page: %s", (page + 1));
                mPresenter.setNextPageToLoad(page + 1);
                mCurrentPage = page + 1;
                mPresenter.loadMoreMovies(page + 1); // The first page is actually 1 not 0, so increment to match the api
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("onResume is called");
        // We can update the filtering from saved state
        mPresenter.setFiltering(mCurrentFilter);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("onActivityCreated is called");
        if (mPresenter != null) mPresenter.setShouldReload(true);
        // restore state if any saved property
        if (savedInstanceState != null) {
            Timber.i("Any saved instance?");
            mCurrentFilter = (MovieFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING);
        } else if (mCurrentFilter == null) {
            mCurrentFilter = MovieFilterType.POPULAR; // Default filter type
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_movies, container, false);
        Timber.i("onCreateView is called");

        // Set up movies view

        mNoMoviesTextView = root.findViewById(R.id.empty_textview);
        mProgressBar = root.findViewById(R.id.loading_progressbar);

        mRecyclerView = root.findViewById(R.id.movie_list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(mOnScrollListener);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Timber.i("onSavedInstanceState is called");
        outState.putInt(CURRENT_PAGE, mCurrentPage);
        outState.putSerializable(CURRENT_FILTERING, mPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.i("onPause is called");
        if (mPresenter.getFiltering() != MovieFilterType.FAVORITES) {
            mPresenter.setShouldReload(false);
        } else {
            mPresenter.setShouldReload(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment_menu, menu);
        // Initialise menu items
        Timber.i("onCreateOptionsMenu is called");
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
                mCurrentFilter = MovieFilterType.TOP_RATED;
                mPresenter.setFiltering(mCurrentFilter);
                if (!item.isChecked()) {
                    // Only reload if the it's not the currently selected filter
                    reloadMovies();
                }
                break;
            case R.id.popularity_filter_menu:
                mCurrentFilter = MovieFilterType.POPULAR;
                mPresenter.setFiltering(mCurrentFilter);
                if (!item.isChecked()) {
                    reloadMovies();
                }
                break;
            case R.id.favorites_filter_menu:
                mCurrentFilter = MovieFilterType.FAVORITES;
                mPresenter.setFiltering(mCurrentFilter);
                if (!item.isChecked()) {
                    Timber.i("Should reload");
                    reloadMovies();
                }
                break;
        }
        // Finally check the item
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Clears adapter and reloads the movies list
     */
    private void reloadMovies() {
        mAdapter.clear();
        ((EndlessScrollListener) mOnScrollListener).resetState();
        mPresenter.reloadMovies();
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
        mAdapter.clear();
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
        // COMPLETED: 9/21/18 create intent and launch the MovieDetail activity
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID_EXTRA, requestedMovieId);
        startActivity(intent);
    }

    public static String getTAG() {
        return TAG;
    }
}
