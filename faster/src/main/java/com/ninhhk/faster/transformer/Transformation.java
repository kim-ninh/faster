package com.ninhhk.faster.transformer;

import android.graphics.Bitmap;

public abstract class Transformation {

    public abstract Bitmap transform(Bitmap source, int targetWidth, int targetHeight);
}
