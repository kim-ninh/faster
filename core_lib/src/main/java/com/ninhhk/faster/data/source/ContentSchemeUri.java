package com.ninhhk.faster.data.source;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ContentSchemeUri extends UriSource {

    private ContentResolver contentResolver;

    public ContentSchemeUri(@NonNull ContentResolver contentResolver,
                            @NonNull Uri uri){
        super(uri);
        this.contentResolver = contentResolver;
    }

    @Override
    public String name() {
        return "uri_" + model;
    }

    @Nullable
    @Override
    public ByteBuffer load(@NonNull Key key, @NonNull Request request) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
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
}
