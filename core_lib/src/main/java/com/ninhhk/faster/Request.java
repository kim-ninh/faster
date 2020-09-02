package com.ninhhk.faster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

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
import java.util.concurrent.Future;

public class Request {

    private final Callback<Bitmap> listener;
    private DataSource<?> dataSource;
    private RequestOption requestOption;
    private WeakReference<ImageView> targetView;
    private ImageDecoder imageDecoder;
    public boolean isLoadFormMem = false;
    public int orientationTag = ExifInterface.ORIENTATION_UNDEFINED;
    private boolean isLoadBitmapOnly = false;

    public Request(Builder builder) {
        this.dataSource = builder.dataSource;
        this.listener = builder.listener;
        this.requestOption = builder.requestOption;
        this.targetView = builder.targetView;
        this.imageDecoder = builder.imageDecoder;
        this.isLoadBitmapOnly = builder.isLoadBitmapOnly;
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

    public boolean isLoadBitmapOnly() {
        return isLoadBitmapOnly;
    }

    public static class Builder {
        public static final String TAG = Builder.class.getSimpleName();
        private final Context context;
        private RequestOption requestOption;
        private WeakReference<ImageView> targetView;
        private DataSource<?> dataSource;
        private Callback listener;
        private ImageLoader imageLoader;
        private ImageDecoder imageDecoder;
        private boolean scaleTypeIsSet = false;
        private boolean isLoadBitmapOnly = false;

        public Builder(@NonNull ImageLoader imageLoader,
                       @NonNull Context context) {
            this.imageLoader = imageLoader;
            this.context = context;
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
            this.dataSource = new DrawableResource(context.getResources(), resId);
            return this;
        }

        public void into(@NonNull ImageView imageView) {
            targetView = new WeakReference<>(imageView);

            imageView.post(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = targetView.get();

                    if (dimensionUnset()) {
                        setDefaultDimensionWithImageView();
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

        public Future<Bitmap> submit(int width, int height) {
            resize(width, height);
            isLoadBitmapOnly = true;
            Request request = build();
            return imageLoader.submitRequest(request);
        }

        public Future<Bitmap> submit(int maxSize){
            resize(maxSize);
            isLoadBitmapOnly = true;
            Request request = build();
            return imageLoader.submitRequest(request);
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

        private void setDefaultDimensionWithImageView() {
            ImageView imageView = this.targetView.get();
            if (imageView != null) {
                int width = imageView.getWidth();
                int height = imageView.getHeight();
                setDefaultDimension(width, height);
            }
        }

        private void setDefaultDimension(int width, int height) {
            if (width != height) {
                resize(width, height);
            } else {
                resize(width);
            }
        }

        private boolean dimensionUnset() {
            return requestOption.isSizeUnset();
        }

        private Request build() {
            return new Request(this);
        }

        public Builder resize(int targetWidth, int targetHeight) {
            requestOption.setFinalHeight(targetHeight);
            requestOption.setFinalWidth(targetWidth);
            imageDecoder = new MaxOneSideImageDecoder();
            return this;
        }

        public Builder resize(int size) {
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
            dataSource = UriSourceFactory.get(context.getContentResolver(), uri);
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
