package com.techbeloved.moviesbeloved.moviedetails.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Review;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.databinding.ReviewItemBinding;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewEntity> mReviewList;

    public void setReviewList(final List<ReviewEntity> reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.review_item, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        // No data yet
        if (mReviewList == null) return;

        Review review = getItem(position);

        holder.binding.setReview(review);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mReviewList != null ? mReviewList.size() : 0;
    }

    private Review getItem(int position) {
        return mReviewList.get(position);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        final ReviewItemBinding binding;

        ReviewViewHolder(@NonNull ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
