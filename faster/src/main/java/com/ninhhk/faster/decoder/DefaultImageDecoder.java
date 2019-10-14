package com.ninhhk.faster.decoder;

import com.ninhhk.faster.RequestOption;

public class DefaultImageDecoder extends ImageDecoder {
    public DefaultImageDecoder(RequestOption requestOption) {
        super(requestOption);
    }

    @Override
    protected void config(byte[] bytes) {
        // default class, do nothing here
    }
}
