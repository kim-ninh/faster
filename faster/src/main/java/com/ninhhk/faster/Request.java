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

import java.lang.ref.WeakReference;

public class Request {

    private final Callback<Bitmap> listener;
    private DataSource<?> dataSource;
    private RequestOption requestOption;
    private WeakReference<ImageView> targetView;
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

    public WeakReference<ImageView> getTargetView() {
        return targetView;
    }

    public ImageDecoder getImageDecoder() {
        return imageDecoder;
    }

    public static class RequestBuilder {
        public static final String TAG = RequestBuilder.class.getSimpleName();
        private RequestOption requestOption;
        private WeakReference<ImageView> targetView;
        private DataSource<?> dataSource;
        private Callback listener;
        private ImageLoader imageLoader;
        private ImageDecoder imageDecoder;
        private boolean scaleTypeIsSet = false;

        public RequestBuilder(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
        }

        private void preLoadConfig() {
            requestOption = new RequestOption();
        }

        public RequestBuilder load(String url) {
            preLoadConfig();
            this.dataSource = new UrlStringSource(url);
            return this;
        }

        public RequestBuilder load(@DrawableRes int resId) {
            preLoadConfig();
            this.dataSource = new DrawableResource(resId);
            return this;
        }

        public void into(@NonNull ImageView imageView) {
            targetView = new WeakReference<>(imageView);
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
            ImageView imageView = this.targetView.get();
            if (imageView != null){
                int width = imageView.getWidth();
                int height = imageView.getHeight();
                resize(width, height);
            }
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
