package com.ninhhk.faster;

import android.graphics.Bitmap;

abstract class Transformation {

    abstract Bitmap transform(Bitmap source, int targetWidth, int targetHeight);
}
