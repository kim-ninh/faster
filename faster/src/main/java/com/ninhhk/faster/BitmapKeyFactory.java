package com.ninhhk.faster;

import com.ninhhk.faster.data.source.DataSource;

public class BitmapKeyFactory extends KeyFactory {

    private Request request;

    public BitmapKeyFactory(Request request) {
        this.request = request;
    }

    @Override
    public Key build() {
        DataSource<?> dataSource = request.getDataSource();
        RequestOption requestOption = request.getRequestOption();

        return new BitmapKey(dataSource, requestOption);
    }
}
