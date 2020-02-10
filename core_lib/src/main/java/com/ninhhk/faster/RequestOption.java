package com.ninhhk.faster;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ninhhk.faster.transformer.DefaultTransformation;
import com.ninhhk.faster.transformer.Transformation;

public class RequestOption {

    private Transformation transformation = new DefaultTransformation();
    private ImageView.ScaleType scaleType;
    private Drawable placeHolderDrawable = null;
    private Size finalSize = new Size();

    public int getFinalWidth() {
        return finalSize.getWidth();
    }

    public void setFinalWidth(int finalWidth) {
        finalSize.setWidth(finalWidth);
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public int getFinalHeight() {
        return finalSize.getHeight();
    }

    public void setFinalHeight(int finalHeight) {
        finalSize.setHeight(finalHeight);
    }

    public void swapSizeRatio() {
        finalSize.swapRatio();
    }

    public boolean isSizeUnset() {
        return finalSize.isUnset();
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

    public void setPlaceHolder(Drawable placeHolderDrawable) {
        this.placeHolderDrawable = placeHolderDrawable;
    }

    @Override
    public String toString() {
        return "RequestOption{" +
                "finalWidth=" + finalSize.getWidth() +
                ", finalHeight=" + finalSize.getHeight() +
                ", transformation=" + transformation +
                '}';
    }

    public Drawable getPlaceHolderDrawable() {
        return placeHolderDrawable;
    }
}
