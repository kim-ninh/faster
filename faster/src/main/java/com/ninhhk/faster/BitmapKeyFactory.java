package com.ninhhk.faster;

import com.ninhhk.faster.data.source.DataSource;

public class BitmapKeyFactory extends KeyFactory {



    @Override
    public Key build(Request request) {
        DataSource<?> dataSource = request.getDataSource();
        RequestOption requestOption = request.getRequestOption();

        return new BitmapKey(dataSource, requestOption);
    }
}
