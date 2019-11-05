package com.ninhhk.faster;

import android.widget.ImageView;

import com.ninhhk.faster.transformer.DefaultTransformation;
import com.ninhhk.faster.transformer.Transformation;

public class RequestOption {

    public static final int UNSET = -1;

    private int finalWidth = UNSET;
    private int finalHeight = UNSET;
    private Transformation transformation = new DefaultTransformation();
    private ImageView.ScaleType scaleType;

    public void setFinalWidth(int finalWidth) {
        this.finalWidth = finalWidth;
    }

    public void setFinalHeight(int finalHeight) {
        this.finalHeight = finalHeight;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public int getFinalWidth() {
        return finalWidth;
    }

    public int getFinalHeight() {
        return finalHeight;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }
}
