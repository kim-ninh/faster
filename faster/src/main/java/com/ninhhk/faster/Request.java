package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Request {

//    private final ResponseDelegate<Bitmap> bitmapLoad;
    private final ResponseDelegate<Bitmap> listener;
    private DataSource<?> dataSource;
    private RequestOption requestOption;
    private ImageView targetView;

    public Request(RequestBuilder builder) {
        this.dataSource = builder.dataSource;
        this.listener = builder.listener;
        this.requestOption = builder.requestOption;
        this.targetView = builder.targetView;
    }

    public ResponseDelegate<Bitmap> getListener() {
        return listener;
    }

    public DataSource<?> getDataSource() {
        return dataSource;
    }

    public RequestOption getRequestOption() {
        return requestOption;
    }

    public ImageView getTargetView() {
        return targetView;
    }

    public static class RequestBuilder {
        public static final String TAG = RequestBuilder.class.getSimpleName();
        public RequestOption requestOption = new RequestOption();
        public ImageView targetView;
        private DataSource<?> dataSource;
        private ResponseDelegate listener;
        private ImageLoader imageLoader;

        public RequestBuilder(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
        }

        public RequestBuilder load(String url) {
            this.dataSource = new UrlStringSource(url);
            return this;
        }

        public void into(final ImageView imageView) {
//            responseDelegate = new ResponseDelegate<Bitmap>() {
//                @Override
//                public void onReady(Bitmap data) {
//                 imageView.setImageBitmap(data);
//                }
//            };
            targetView = imageView;
            if (dimesionUnset()) {
                setDefaultDimension();
            }

            Request request = build();
            imageLoader.handleRequest(request);
        }

        private void setDefaultDimension() {
            requestOption.setFinalWidth(targetView.getWidth());
            requestOption.setFinalHeight(targetView.getHeight());
        }

        private boolean dimesionUnset() {
            return requestOption.getFinalHeight() == RequestOption.UNSET ||
            requestOption.getFinalWidth() == RequestOption.UNSET;
        }

        private Request build(){
            return new Request(this);
        }

        public RequestBuilder resize(int targetWidth, int targetHeight) {
            requestOption.setFinalHeight(targetHeight);
            requestOption.setFinalWidth(targetWidth);
            return this;
        }

        public RequestBuilder onReady(ResponseDelegate responseDelegate) {
//            this.responseDelegate = responseDelegate;
            return this;
        }

        public RequestBuilder setListener(ResponseDelegate<Bitmap> listener) {
            this.listener = listener;
            return this;
        }
    }

}
