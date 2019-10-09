package com.ninhhk.faster;

import android.graphics.Bitmap;

public class DefaultTransformation extends Transformation {
    @Override
    Bitmap transform(Bitmap source, int targetWidth, int targetHeight) {
        return source;
    }
}
