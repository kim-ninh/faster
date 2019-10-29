package com.ninhhk.faster.data.source;

import android.net.Uri;

public abstract class UriSource extends DataSource<Uri> {
    public UriSource(Uri model) {
        super(model);
    }
}
