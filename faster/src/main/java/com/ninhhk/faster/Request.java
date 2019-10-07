package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Request {

    private final ResponseDelegate<Bitmap> bitmapLoad;
    private final ResponseDelegate<Bitmap> listener;
    private DataSource<?> dataSource;
    private List<RequestOption> requestOptions;
    private ImageDecoder imageDecoder;

    public Request(RequestBuilder builder) {
        this.dataSource = builder.dataSource;
        this.bitmapLoad = builder.responseDelegate;
        this.listener = builder.listener;
        this.requestOptions = builder.requestOptions;
        this.imageDecoder = builder.imageDecoder;
    }

    public void start() {
        dataSource.setByteLoadSuccess((bytes) -> {
            Bitmap bm = imageDecoder.decode(bytes);
            bitmapLoad.onReady(bm);
            if (listener != null) {
                listener.onReady(bm);
            }
        });

        dataSource.load();
    }

    public void cancel() {

    }

    public static class RequestBuilder {
        public static final String TAG = RequestBuilder.class.getSimpleName();
        private ImageDecoder imageDecoder = new ImageDecoder();
        private DataSource<?> dataSource;
        private ResponseDelegate responseDelegate;
        private ResponseDelegate listener;
        private List<RequestOption> requestOptions = new ArrayList<>();

        public RequestBuilder load(String url) {
            this.dataSource = new UrlStringSource(url);
            return this;
        }

        public void into(final ImageView imageView) {
            responseDelegate = new ResponseDelegate<Bitmap>() {
                @Override
                public void onReady(Bitmap data) {
                    imageView.setImageBitmap(data);
                }
            };

            Request request = build();
            request.start();
        }

        private Request build() {
            return new Request(this);
        }

        public RequestBuilder resize(int targetWidth, int targetHeight) {
            return this;
        }

        public RequestBuilder onReady(ResponseDelegate responseDelegate) {
            this.responseDelegate = responseDelegate;
            return this;
        }

        public RequestBuilder setListener(ResponseDelegate<Bitmap> listener) {
            this.listener = listener;
            return this;
        }
    }

}
