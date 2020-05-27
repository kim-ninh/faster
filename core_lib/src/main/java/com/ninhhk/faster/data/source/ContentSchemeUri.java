package com.ninhhk.faster.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Request;
import com.ninhhk.faster.data.store.ByteBufferPool;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.MemoryUtils;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ContentSchemeUri extends UriSource {

    public ContentSchemeUri(Uri model) {
        super(model);
    }

    @NonNull
    @Override
    public ByteBuffer loadToBuffer(@NonNull Context context,
                                   @NonNull Request request) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
        ContentResolver contentResolver = context.getContentResolver();
        InputStream is;
        try {
            is = contentResolver.openInputStream(model);
            if (is == null)
                return byteBuffer;

            request.orientationTag = ExifUtils.getOrientationTag(is);
            is.close();

            is = contentResolver.openInputStream(model);
            if (is == null)
                return byteBuffer;

            byteBuffer = StreamUtils.readToBuffer(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer;
    }

    @Override
    public String name() {
        return "uri_" + model;
    }

}
