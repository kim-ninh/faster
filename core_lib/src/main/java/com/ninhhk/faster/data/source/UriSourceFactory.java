package com.ninhhk.faster.data.source;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;

public class UriSourceFactory {
    public static DataSource<?> get(@NonNull ContentResolver contentResolver,
                                    @NonNull Uri uri) {
        if (uri.getScheme().equals("content")){
            return new ContentSchemeUri(contentResolver, uri);
        }
        else if (uri.getScheme().equals("http") || uri.getScheme().equals("https")){
            return new UrlStringSource(uri.toString());
        }

        return null;
    }
}
