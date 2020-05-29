package com.ninhhk.faster;

import androidx.annotation.NonNull;

public interface Cacheable<T> {

    void cache(@NonNull Key key,
               @NonNull T data);
}
