package com.techbeloved.moviesbeloved.movies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;

public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);

    }

    @BindingAdapter("visibleGone")
    public static void showOrHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Used to update text view with numbers such as float
    @BindingAdapter("android:text")
    public static void setText(TextView textView, Number numberValue) {
        textView.setText(String.valueOf(numberValue));
    }

}
