package com.ninhhk.faster.data.source;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.ninhhk.faster.Request;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DrawableResource extends DataSource<Integer> {

    public DrawableResource(@DrawableRes int resId) {
        super(resId);
    }

    @NonNull
    @Override
    public ByteBuffer loadToBuffer(@NonNull Context context,
                                   @NonNull Request request) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        Resources resources = context.getResources();
        InputStream is;
        try {
            is = resources.openRawResource(model);
            request.orientationTag = ExifUtils.getOrientationTag(is);
            is.close();

            is = resources.openRawResource(model);
            byteBuffer = StreamUtils.readToBuffer(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer;
    }

    @Override
    public String name() {
        return "resId_" + model;
    }

}
