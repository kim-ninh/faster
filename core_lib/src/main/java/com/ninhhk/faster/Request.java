package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.ninhhk.faster.data.source.DataSource;
import com.ninhhk.faster.data.source.DrawableResource;
import com.ninhhk.faster.data.source.FileSource;
import com.ninhhk.faster.data.source.UriSourceFactory;
import com.ninhhk.faster.data.source.UrlStringSource;
import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.decoder.MatchAreaImageDecoder;
import com.ninhhk.faster.decoder.MaxOneSideImageDecoder;
import com.ninhhk.faster.transformer.Transformation;
import com.ninhhk.faster.transformer.TransformationFactory;

import java.io.File;
import java.lang.ref.WeakReference;

public class Request {

    private final Callback<Bitmap> listener;
    private DataSource<?> dataSource;
    private RequestOption requestOption;
    private WeakReference<ImageView> targetView;
    private ImageDecoder imageDecoder;
    public boolean isLoadForMem = false;
    public int exifOrientation = 0;

    public Request(Builder builder) {
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

    public static class Builder {
        public static final String TAG = Builder.class.getSimpleName();
        private RequestOption requestOption;
        private WeakReference<ImageView> targetView;
        private DataSource<?> dataSource;
        private Callback listener;
        private ImageLoader imageLoader;
        private ImageDecoder imageDecoder;
        private boolean scaleTypeIsSet = false;

        public Builder(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
        }

        private void preLoadConfig() {
            requestOption = new RequestOption();
            scaleTypeIsSet = false;
        }

        public Builder load(String url) {
            preLoadConfig();
            this.dataSource = new UrlStringSource(url);
            return this;
        }

        public Builder load(@DrawableRes int resId) {
            preLoadConfig();
            this.dataSource = new DrawableResource(resId);
            return this;
        }

        public void into(@NonNull ImageView imageView) {
            targetView = new WeakReference<>(imageView);

            imageView.post(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = targetView.get();

                    if (dimensionUnset()) {
                        setDefaultDimension();
                    }

                    if (scaleTypeUnSet()) {
                        setDefaultScaleType(imageView);
                    }

                    if (placeHoladerIsSet()) {
                        imageView.setImageDrawable(requestOption.getPlaceHolderDrawable());
                    }
                    Request request = build();
                    imageLoader.handleRequest(request);
                }
            });
        }

        private boolean placeHoladerIsSet() {
            return requestOption.getPlaceHolderDrawable() != null;
        }

        private void setDefaultScaleType(ImageView imageView) {
            ImageView.ScaleType scaleType = imageView.getScaleType();
            transform(scaleType);
            requestOption.setScaleType(scaleType);
        }

        private boolean scaleTypeUnSet() {
            return !scaleTypeIsSet;
        }

        private void setDefaultDimension() {
            ImageView imageView = this.targetView.get();
            if (imageView != null){
                int width = imageView.getWidth();
                int height = imageView.getHeight();
                if (width != height) {
                    resize(width, height);
                }
                else {
                    resize(width);
                }
            }
        }

        private boolean dimensionUnset() {
            return requestOption.getFinalHeight() == RequestOption.UNSET ||
            requestOption.getFinalWidth() == RequestOption.UNSET;
        }

        private Request build(){
            return new Request(this);
        }

        public Builder resize(int targetWidth, int targetHeight) {
            requestOption.setFinalHeight(targetHeight);
            requestOption.setFinalWidth(targetWidth);
            imageDecoder = new MaxOneSideImageDecoder();
            return this;
        }

        public Builder resize(int size){
            this.resize(size, size);
            imageDecoder = new MatchAreaImageDecoder();
            return this;
        }

        public Builder setListener(Callback<Bitmap> listener) {
            this.listener = listener;
            return this;
        }

        public Builder transform(ImageView.ScaleType scaleType) {
            scaleTypeIsSet = true;
            Transformation transformation = TransformationFactory.get(scaleType);
            requestOption.setTransformation(transformation);
            requestOption.setScaleType(scaleType);
            return this;
        }

        public Builder placeholder(Drawable placeHolderDrawable) {
            requestOption.setPlaceHolder(placeHolderDrawable);
            return this;
        }

        public Builder load(Uri uri) {
            preLoadConfig();
            dataSource = UriSourceFactory.get(uri);
            return this;
        }

        public Builder load(File file) {
            preLoadConfig();
            dataSource = new FileSource(file);
            return this;
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "dataSource=" + dataSource +
                ", requestOption=" + requestOption +
                '}';
    }
}
