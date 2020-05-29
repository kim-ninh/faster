package com.ninhhk.faster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Loadable<T> {

    @Nullable
    T load(@NonNull Key key, @NonNull Request request);
}
