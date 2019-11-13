package com.ninhhk.faster.transformer;

import android.graphics.Matrix;

public class DefaultTransformation extends Transformation {
    @Override
    protected Matrix getMatrix(int targetWidth, int targetHeight, int bWidth, int bHeight) {
        Matrix matrix = new Matrix();
        return matrix;
    }

    @Override
    public String toString() {
        return "DefaultTransformation{}";
    }
}
