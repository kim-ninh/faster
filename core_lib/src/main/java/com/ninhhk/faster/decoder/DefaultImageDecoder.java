package com.ninhhk.faster.decoder;

public class DefaultImageDecoder extends ImageDecoder {

    @Override
    protected int[] config(byte[] bytes, int offset, int length) {
        return new int[]{0, 0};
    }

}
