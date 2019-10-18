package com.ninhhk.faster.transformer;

import android.widget.ImageView;

public class TransformationFactory {
    public static Transformation get(ImageView.ScaleType scaleType) {

        if (scaleType == ImageView.ScaleType.CENTER)
            return new CenterTransformation();

        return null;
    }
}
