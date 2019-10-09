package com.ninhhk.faster;

public class DefaultImageDecoder extends ImageDecoder {
    public DefaultImageDecoder(RequestOption requestOption) {
        super(requestOption);
    }

    @Override
    protected void config(byte[] bytes) {
        // default class, do nothing here
    }
}
