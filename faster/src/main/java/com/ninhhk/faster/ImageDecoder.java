package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageDecoder {
    BitmapFactory.Options opts = new BitmapFactory.Options();

    protected void config() {
        opts = new BitmapFactory.Options();
    }

    public Bitmap decode(byte[] bytes) {
        config();
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }
}
