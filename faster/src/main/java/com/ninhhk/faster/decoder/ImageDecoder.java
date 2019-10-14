package com.ninhhk.faster.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ninhhk.faster.RequestOption;
import com.ninhhk.faster.transformer.Transformation;

public abstract class ImageDecoder {
    protected BitmapFactory.Options opts = new BitmapFactory.Options();
    protected RequestOption requestOption;

    public ImageDecoder(RequestOption requestOption) {
        this.requestOption = requestOption;
    }

    public ImageDecoder(){

    }

    abstract protected void config(byte[] bytes);

    public Bitmap decode(byte[] bytes, RequestOption requestOption) {
        Bitmap result;
        this.requestOption = requestOption;

        config(bytes);
        opts.inJustDecodeBounds = false;
        result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        result = applyTransformation(result);
        return result;
    }

    private Bitmap applyTransformation(Bitmap source) {
        Transformation transformation = requestOption.getTransformation();
        return transformation.transform(source, requestOption.getFinalWidth(), requestOption.getFinalHeight());
    }
}
