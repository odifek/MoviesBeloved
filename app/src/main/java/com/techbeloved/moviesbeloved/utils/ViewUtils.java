package com.techbeloved.moviesbeloved.utils;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class ViewUtils {
    private static final String TAG = ViewUtils.class.getSimpleName();
    public static void fadeOutView(final View view) {
        if (view.getAnimation() == null || view.getAnimation().hasEnded()) {
            Log.i(TAG, "fadeOutView: ");
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setDuration(1000);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            view.startAnimation(fadeOut);
        }
    }

    public static void fadeInView(final View view) {
        if (view.getAnimation() == null || view.getAnimation().hasEnded()) {
            Log.i(TAG, "fadeInView: ");
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new AccelerateInterpolator());
            fadeIn.setDuration(1000);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            view.startAnimation(fadeIn);
        }
    }
}
