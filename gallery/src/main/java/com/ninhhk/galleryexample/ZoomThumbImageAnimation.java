package com.ninhhk.galleryexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

class ZoomThumbImageAnimation {

    private Animator currentAnimator;
    private int shortAnimationDuration = 250; // in milliseconds

    private final Rect startBounds = new Rect();
    private final Rect finalBounds = new Rect();
    private final float startScale;

    ZoomThumbImageAnimation(@NonNull View thumbImage,
                            @NonNull View containerView) {
        Point globalOffset = new Point();

        thumbImage.getGlobalVisibleRect(startBounds);
        containerView.getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {

            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {

            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
    }

    void zoomIn(@NonNull View expandedImage){
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        expandedImage.setVisibility(View.VISIBLE);

        expandedImage.setPivotX(0f);
        expandedImage.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_Y,
                        startScale, 1f));

        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }
        });

        set.start();
        currentAnimator = set;
    }

    void zoomOut(@NonNull View expandedImage){
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        final float startScaleFinal = startScale;

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImage, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImage, View.Y, startBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_Y, startScaleFinal));

        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                expandedImage.setVisibility(View.INVISIBLE);
                currentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                expandedImage.setVisibility(View.INVISIBLE);
                currentAnimator = null;
            }
        });

        set.start();
        currentAnimator = set;
    }
}
