package com.techbeloved.moviesbeloved.moviedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Review;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        // No data yet
        if (mReviewList == null) return;

        Review review = getItem(position);

        holder.reviewAuthor.setText(review.getAuthor());

        holder.reviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList != null ? mReviewList.size() : 0;
    }

    private Review getItem(int position) {
        return mReviewList.get(position);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.author_text);
            reviewContent = itemView.findViewById(R.id.review_text);
        }
    }
}
