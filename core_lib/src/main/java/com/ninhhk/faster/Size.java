package com.ninhhk.faster;

public class Size {

    public static int UNSET = -1;
    private int width = UNSET;
    private int height = UNSET;

    Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    Size() {

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width < 0)
            throw new IllegalArgumentException("width must >= 0");
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height < 0)
            throw new IllegalArgumentException("height must >= 0");
        this.height = height;
    }

    public boolean isUnset() {
        return width == UNSET || height == UNSET;
    }

    public void swapRatio() {
        int tmp = width;
        width = height;
        height = tmp;
    }
}
