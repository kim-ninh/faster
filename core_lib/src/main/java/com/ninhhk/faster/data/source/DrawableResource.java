package com.ninhhk.faster.data.source;

import android.content.res.Resources;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DrawableResource extends DataSource<Integer> {

    private Resources resources;

    public DrawableResource(@NonNull Resources resources,
                            @DrawableRes int resId){
        super(resId);
        this.resources = resources;
    }

    @Override
    public String name() {
        return "resId_" + model;
    }

    @Nullable
    @Override
    public ByteBuffer load(@NonNull Key key, @NonNull Request request) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
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
}
