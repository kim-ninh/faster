package com.ninhhk.faster.transformer;

import android.widget.ImageView;

public class TransformationFactory {
    public static Transformation get(ImageView.ScaleType scaleType) {

        if (scaleType == ImageView.ScaleType.CENTER)
            return new CenterTransformation();

        if (scaleType == ImageView.ScaleType.CENTER_CROP)
            return new CenterCropTransformation();

        if (scaleType == ImageView.ScaleType.FIT_CENTER)
            return new FitCenterTransformation();

        if (scaleType == ImageView.ScaleType.MATRIX)
            return new DefaultTransformation();

        return new DefaultTransformation();
    }
}
