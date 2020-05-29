package com.ninhhk.faster;

import androidx.annotation.NonNull;

public interface Callback<T> {
    void onReady(@NonNull T data);
}
