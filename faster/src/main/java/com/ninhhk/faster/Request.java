package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.ninhhk.faster.data.source.DataSource;
import com.ninhhk.faster.data.source.DrawableResource;
import com.ninhhk.faster.data.source.UrlStringSource;
import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.decoder.MatchAreaImageDecoder;
import com.ninhhk.faster.decoder.MaxOneSideImageDecoder;
import com.ninhhk.faster.transformer.Transformation;
import com.ninhhk.faster.transformer.TransformationFactory;

public class Request {

//    private final Callback<Bitmap> bitmapLoad;
    private final Callback<Bitmap> listener;
    private DataSource<?> dataSource;
    private RequestOption requestOption;
    private ImageView targetView;
    private ImageDecoder imageDecoder;

    public Request(RequestBuilder builder) {
        this.dataSource = builder.dataSource;
        this.listener = builder.listener;
        this.requestOption = builder.requestOption;
        this.targetView = builder.targetView;
        this.imageDecoder = builder.imageDecoder;
    }

    public Callback<Bitmap> getListener() {
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

    public ImageDecoder getImageDecoder() {
        return imageDecoder;
    }

    public static class RequestBuilder {
        public static final String TAG = RequestBuilder.class.getSimpleName();
        public RequestOption requestOption = new RequestOption();
        public ImageView targetView;
        private DataSource<?> dataSource;
        private Callback listener;
        private ImageLoader imageLoader;
        private ImageDecoder imageDecoder;
        private boolean scaleTypeIsSet = false;

        public RequestBuilder(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
        }

        public RequestBuilder load(String url) {
            this.dataSource = new UrlStringSource(url);
            return this;
        }

        public RequestBuilder load(@DrawableRes int resId) {
            this.dataSource = new DrawableResource(resId);
            return this;
        }

        public void into(@NonNull final ImageView imageView) {
//            responseDelegate = new Callback<Bitmap>() {
//                @Override
//                public void onReady(Bitmap data) {
//                 imageView.setImageBitmap(data);
//                }
//            };
            targetView = imageView;
            if (dimesionUnset()) {
                setDefaultDimension();
            }

            if (scaleTypeUnSet()) {
                setDefaultScaleType(imageView);
            }

            Request request = build();
            imageLoader.handleRequest(request);
        }

        private void setDefaultScaleType(ImageView imageView) {
            ImageView.ScaleType scaleType = imageView.getScaleType();
            setScaleType(scaleType);
        }

        private boolean scaleTypeUnSet() {
            return !scaleTypeIsSet;
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
            imageDecoder = new MaxOneSideImageDecoder();
            return this;
        }

        public RequestBuilder resize(int size){
            this.resize(size, size);
            imageDecoder = new MatchAreaImageDecoder();
            return this;
        }

        public RequestBuilder onReady(Callback callback) {
//            this.callback = callback;
            return this;
        }

        public RequestBuilder setListener(Callback<Bitmap> listener) {
            this.listener = listener;
            return this;
        }

        public RequestBuilder setScaleType(ImageView.ScaleType scaleType){
            scaleTypeIsSet = true;
            Transformation transformation = TransformationFactory.get(scaleType);
            requestOption.setTransformation(transformation);
            return this;
        }
    }

}
