package com.ninhhk.faster.transformer;

import android.graphics.Matrix;

public class FitCenterTransformation extends Transformation {
    @Override
    protected Matrix getMatrix(int targetWidth, int targetHeight, int bWidth, int bHeight) {
        Matrix matrix = new Matrix();

        float scale;
        float dx = 0, dy = 0;

        if (targetHeight * bWidth > bHeight * targetWidth) {
            scale = (float) targetWidth / (float) bWidth;
            dy = (targetHeight - bHeight * scale) * 0.5f;
        } else {
            scale = (float) targetHeight / (float) bHeight;
            dx = (targetWidth - bWidth * scale) * 0.5f;
        }

        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);

        return matrix;
    }

    @Override
    public String toString() {
        return "FitCenterTransformation{}";
    }
}
