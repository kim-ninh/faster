package com.ninhhk.faster.transformer;

import android.graphics.Matrix;

public class CenterTransformation extends Transformation {

    @Override
    protected Matrix getMatrix(int targetWidth, int targetHeight, int bWidth, int bHeight) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(Math.round((targetWidth - bWidth) * 0.5f),
                Math.round((targetHeight - bHeight) * 0.5f));
        return matrix;
    }
}
