package com.ninhhk.faster;

import android.graphics.Bitmap;

abstract class Transformation {
    protected Bitmap bm;

    public Transformation(Bitmap bm) {
        this.bm = bm;
    }

    abstract void transform();
}
