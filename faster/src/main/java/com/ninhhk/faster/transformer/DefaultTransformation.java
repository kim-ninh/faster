package com.ninhhk.faster.transformer;

import android.graphics.Bitmap;

public class DefaultTransformation extends Transformation {
    @Override
    public Bitmap transform(Bitmap source, int targetWidth, int targetHeight) {
        return source;
    }
}
