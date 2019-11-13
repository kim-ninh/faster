package com.ninhhk.faster.decoder;

public class DefaultImageDecoder extends ImageDecoder {

    @Override
    protected int[] config(byte[] bytes) {
        // default class, do nothing here
        return new int[]{0, 0};
    }
}
