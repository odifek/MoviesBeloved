package com.techbeloved.moviesbeloved.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.databinding.FragmentMoviesBinding;
import com.techbeloved.moviesbeloved.di.ActivityScope;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;
import com.techbeloved.moviesbeloved.utils.EndlessScrollListener;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;

import static com.techbeloved.moviesbeloved.MovieFilterType.*;
import static com.techbeloved.moviesbeloved.common.viewmodel.Status.ERROR;
import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_ID_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ActivityScope
public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private MovieAdapter mAdapter;
    RecyclerView.OnScrollListener mOnScrollListener;
    private GridLayoutManager mLayoutManager;

    private MoviesViewModel mViewModel;

    @Inject
    public MoviesViewModelFactory mViewModelFactory;

    @Inject
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
        mAdapter = new MovieAdapter(movie -> {
            // COMPLETED: 9/21/18 implement onclick
//            mPresenter.openMovieDetails(movie.getId());
        });
        mLayoutManager = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.movie_grid_span_count));
        mOnScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Timber.i("Loading page: %s", (page + 1));
                setNextPageToLoad(getCurrentPage() + 1);
                setShouldLoadNextPage();
                // FIXME: 10/12/18 onLoadMore should be triggered each time even when there is no network
            }
        };
    }

    private void setShouldLoadNextPage() {
        mViewModel.setShouldLoadNextPage(true);
    }

    private int getCurrentPage() {
        return mViewModel.getCurrentPage();
    }

    private void setNextPageToLoad(int page) {
        mViewModel.setNextPage(page);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mPresenter.result(requestCode, resultCode);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("onActivityCreated is called");

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MoviesViewModel.class);
        subscribeUi(mViewModel);
    }

    private void subscribeUi(MoviesViewModel viewModel) {
        viewModel.getMovieCollectionResponse().observe(this, this::processResponse);
        viewModel.response().observe(this, listResponse -> {
            if (listResponse.status == ERROR) {
                listResponse.error.printStackTrace();
//                // We want the Endless scroll to try again
//                ((EndlessScrollListener) mOnScrollListener).resetLoadingState();
                showNetworkError();
            }
        });
    }

    private void showNetworkError() {
        Toast.makeText(getContext(), "Network Error: unable to load movies", Toast.LENGTH_SHORT).show();
    }

    private void processResponse(Response<List<MovieEntity>> listResponse) {
        Timber.i("Called to process response");
        switch (listResponse.status) {
            case LOADING:
                setLoadingIndicator(true);
                break;
            case SUCCESS:
                showMovies(listResponse.data);
                Timber.i("Got some data");
                break;
            case ERROR:
                listResponse.error.printStackTrace();
                Timber.e("Ouch! Something bad happened");
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
        mBinding.movieList.setOnScrollListener(mOnScrollListener);
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
        }
    }

    private MovieFilterType getFiltering() {
        return mViewModel.getFiltering();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated_filter_menu:
                setFiltering(TOP_RATED);
                if (!item.isChecked()) {
                    // Only reload if the item is not the currently selected filter
                    reloadMovies();
                }
                break;
            case R.id.popularity_filter_menu:
                setFiltering(POPULAR);
                if (!item.isChecked()) {
                    reloadMovies();
                }
                break;
            case R.id.favorites_filter_menu:
                setFiltering(FAVORITES);
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

    private void setFiltering(MovieFilterType filterType) {
        mViewModel.setFilterType(filterType);
    }

    /**
     * Clears adapter and reloads the movies list
     */
    private void reloadMovies() {
        mAdapter.clear();
        ((EndlessScrollListener) mOnScrollListener).resetState();
        // ViewModel is reloaded automatically. So no need to do any other thing
    }

    public void setLoadingIndicator(boolean active) {
        mBinding.setIsLoading(active);
    }

    public void showMovies(List<MovieEntity> movies) {
        setLoadingIndicator(false);
        mAdapter.updateMovieList(movies);
    }

    public void showLoadingMoviesError() {
        setLoadingIndicator(false);
        mBinding.emptyTextview.setText("Error loading movies");
        mBinding.setIsEmpty(true);
    }


    public void showNoMovies() {
        mBinding.setIsEmpty(true);
    }


    public boolean isActive() {
        // Fragment is currently active
        return isAdded();
    }


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
