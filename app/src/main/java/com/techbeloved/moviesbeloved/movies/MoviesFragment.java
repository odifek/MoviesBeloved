package com.techbeloved.moviesbeloved.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.MoviesApp;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.databinding.FragmentMoviesBinding;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;

import javax.inject.Inject;

import timber.log.Timber;

import static com.techbeloved.moviesbeloved.MovieFilterType.FAVORITES;
import static com.techbeloved.moviesbeloved.MovieFilterType.LATEST;
import static com.techbeloved.moviesbeloved.MovieFilterType.POPULAR;
import static com.techbeloved.moviesbeloved.MovieFilterType.TOP_RATED;
import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_ID_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private MovieAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private MoviesViewModel mViewModel;

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
     *
     * @param savedInstanceState is the saved instance bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MoviesApp) requireActivity().getApplication()).getAppComponent().inject(this);
        mAdapter = new MovieAdapter(movie -> {
            // COMPLETED: 9/21/18 implement onclick
            Timber.i("Movie Id is: %s", movie.getId());
            showMovieDetails(movie.getId());
        });
        mLayoutManager = new GridLayoutManager(getContext(), requireContext().getResources().getInteger(R.integer.movie_grid_span_count));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("onActivityCreated is called");

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MoviesViewModel.class);
        subscribeUi(mViewModel);
    }

    private void subscribeUi(MoviesViewModel viewModel) {
        viewModel.response().observe(getViewLifecycleOwner(), this::processResponse);
    }

    private void processResponse(Response<PagedList<MovieEntity>> listResponse) {
        Timber.i("Called to process response");
        switch (listResponse.status) {
            case LOADING:
                setLoadingIndicator(true);
                Timber.i("Should show loading");
                break;
            case SUCCESS:
                showMovies(listResponse.data);
                Timber.i("Got some data");
                break;
            case ERROR:
                Timber.e(listResponse.error, "Ouch! Something bad happened");
                showLoadingMoviesError();
                break;
        }
    }

    private FragmentMoviesBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        mBinding.movieList.setAdapter(mAdapter);
        mBinding.movieList.setLayoutManager(mLayoutManager);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment_menu, menu);
        // Initialise menu items
        Timber.i("onCreateOptionsMenu is called");
        // DONE: 10/9/18 We are only testing with favorites for now
        menu.findItem(R.id.favorites_filter_menu).setChecked(true);
        switch (getFiltering()) {
            case TOP_RATED:
                menu.findItem(R.id.top_rated_filter_menu).setChecked(true);
                break;
            case POPULAR:
                menu.findItem(R.id.popularity_filter_menu).setChecked(true);
                break;
            case FAVORITES:
                menu.findItem(R.id.favorites_filter_menu).setChecked(true);
                break;
            case LATEST:
                menu.findItem(R.id.latest_filter_menu).setChecked(true);
                break;
        }
    }

    private MovieFilterType getFiltering() {
        return mViewModel.getFiltering();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated_filter_menu:
                if (!item.isChecked()) {
                    // Only reload if the item is not the currently selected filter
                    setFiltering(TOP_RATED);
                }
                break;
            case R.id.popularity_filter_menu:
                if (!item.isChecked()) {
                    setFiltering(POPULAR);
                }
                break;
            case R.id.favorites_filter_menu:
                if (!item.isChecked()) {
                    Timber.i("Should reload");
                    setFiltering(FAVORITES);
                }
                break;
            case R.id.latest_filter_menu:
                if (!item.isChecked()) {
                    setFiltering(LATEST);
                }
        }
        // Finally check the item
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    private void setFiltering(MovieFilterType filterType) {
        mViewModel.setFilterType(filterType);
    }

    public void setLoadingIndicator(boolean active) {
        mBinding.setIsLoading(active);
    }

    public void showMovies(PagedList<MovieEntity> movies) {
        setLoadingIndicator(false);
        mAdapter.submitList(movies);
    }

    public void showLoadingMoviesError() {
        setLoadingIndicator(false);
        mBinding.emptyTextview.setText("Error loading movies");
        mBinding.setIsEmpty(true);
    }


    public void showNoMovies() {
        mBinding.setIsEmpty(true);
    }

    public void showMovieDetails(int requestedMovieId) {
        // COMPLETED: 9/21/18 create intent and launch the MovieDetail activity
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID_EXTRA, requestedMovieId);
        startActivity(intent);
    }
}
