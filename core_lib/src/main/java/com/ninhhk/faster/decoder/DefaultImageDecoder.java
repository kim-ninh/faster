package com.ninhhk.faster.decoder;

import java.io.InputStream;

public class DefaultImageDecoder extends ImageDecoder {

    @Override
    protected int[] config(byte[] bytes) {
        // default class, do nothing here
        return new int[]{0, 0};
    }

    @Override
    protected int[] config(InputStream is) {
        return new int[]{0, 0};
    }
}
