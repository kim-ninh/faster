package com.ninhhk.faster;

public class RequestOption {

    public static final int UNSET = -1;

    private int finalWidth = UNSET;
    private int finalHeight = UNSET;
    private Transformation transformation = new DefaultTransformation();

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
}
